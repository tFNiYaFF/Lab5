#include "calculate.h"

static void DinamicMu_M_B1(const uint64_t lower, const uint64_t upper) {
	double* F_B = (double*)calloc(B_TOTAL, sizeof(double));
	double* E_f_meV = (double*)calloc(LANDAU_TOTAL_LEVELS, sizeof(double));

	for (int i = lower; i < upper; i++) {
		const double B = B_MIN + i * B_DELTA; // Просто выбираем текущее значение B
		const double Gamma = GAMMA_WEIGHT(B); // Посчитали ширину уровня Г от текущего B
		const double mu_star = LOOP_FR(B);	  // Вычисляем мю*_б для приближенного значения (для Ef)
		for (int n = 0; n <= LANDAU_TOTAL_LEVELS - 1; n++) {
			E_f_meV[n] = H_RED * mu_star * (2 * n + 1) / (2 * K_SGS_MEV); // Вычисляем некое приближенное теор. значение Ef для начала подсчета интеграла перебором в цикле по мю.
		}
		const double E_max_meV = E_f_meV[LANDAU_TOTAL_LEVELS - 1] + H_RED * mu_star; // Максимальное значение теор. приближения.
		const double g = LANDAU(B); // Кратность выраждения уровня ландау
		const int n_f = GetLandauLevel(B) - 1; // nomer urovnya Landau
		const double mu_min = E_f_meV[n_f] * 0.6; // minimal'noe znachenie diapazona mu
		const double mu_max = E_f_meV[n_f] * 1.4; // maximal'noe znachenie diapazona mu
		double F_full = 0.0; // tekuschii termodinamicheskii potencial pri integrirovanii
		double M_B = 0.0;
		double muMin = mu_min; // mu, realizuyuschee minimal'noe otklonenie ot Nstruct
		double diffMin = 2.0E+22; // minimal'noe otklonenie chisla chastic ot Nstruct

		for (int jj = 0; jj <= MU_POINTS; jj++) {
			double mu = mu_min + jj * (mu_max - mu_min) / MU_POINTS;
			//-----------------
			double mu_erg = mu * K_SGS_MEV; // Текущее значение mu в эргах 
			//-----------------
			double N = 0.0; // значение числа частиц N.
			for (int j = 0; j <= E_POINTS; j++) {
				const double E_meV = j * E_max_meV / E_POINTS; // Текущее значение энергии E.
				//----------------------------------
				const double E_erg = E_meV * K_SGS_MEV; // Текущее значение энергии E в эргах
				//const double FD_E = FERMI_DIARAK(E_meV, mu); // Считаем текущее ззначение функции Ферми Диарака от текущего значения энергии.
				const double FD_E = FERMI_DIARAK(E_erg, mu_erg);
				//----------------------------------
				double D_E = 0.0; // Далее считаем D(E)
				for (int ikk = 0; ikk <= LANDAU_TOTAL_LEVELS - 1; ikk++) {
					D_E = D_E + D(E_meV, E_f_meV[ikk], Gamma); // Считаем D(E) - в мэВ!
				}
				//---------------------
				//double podint_nkv = g * D_E * FD_E; // Получаем итоговую формулу N для интегрирования
				double podint_nkv = g * D_E * FD_E / K_SGS_MEV;
				//---------------------
				Integrate(j, &N, podint_nkv); // Интегрируем и получаем какое то N
			}
			N = N * E_max_meV * K_SGS_MEV  / E_POINTS;
			const double diff = fabs(N - HOLES_NUM);
			if (fabs(diff) < fabs(diffMin)) {
				diffMin = diff;
				muMin = mu;
			}
		} 
			//------------------
			double muMin_erg = muMin * K_SGS_MEV; // Окончательное значение mu в эргах 
			//------------------
			
		for (int j = 0; j <= E_POINTS; j++) {
			double E_meV = j * E_max_meV / E_POINTS;
			//------------------
			//double f_FD = FERMI_DIARAK(E_meV, muMin);
			double E_erg = E_meV * K_SGS_MEV;
			double f_FD = FERMI_DIARAK(E_erg, muMin_erg);
			//------------------
			double D_en_meV = 0.0;
			for (int ikk = 0; ikk <= LANDAU_TOTAL_LEVELS - 1; ikk++) { 
				D_en_meV = D_en_meV + D(E_meV, E_f_meV[ikk], Gamma);
			}
			//------------------
			double D_en_erg = D_en_meV / K_SGS_MEV;
			//double podint_F = -g * BOLTZMANN_CONSTANT * T * D_en_meV * log(1.0 + exp((-E_meV + muMin) / (BOLTZMANN_CONSTANT * T)));
			double podint_F = -g * BOLTZMANN_CONSTANT * T * D_en_erg * log(1.0 + exp((-E_erg + muMin_erg) / (BOLTZMANN_CONSTANT * T)));
			//------------------
			Integrate(j, &F_full, podint_F);
		}

		F_B[i] = F_full * E_max_meV / E_POINTS;

		//if (i != 0 && fabs(B - B_1) > NON_CRIT_B && fabs(B - B_2) > NON_CRIT_B && fabs(B - B_3) > NON_CRIT_B
		//	&& fabs(B - B_4) > NON_CRIT_B && fabs(B - B_5) > NON_CRIT_B && fabs(B - B_6) > NON_CRIT_B
		//	&& fabs(B - B_7) > NON_CRIT_B && fabs(B - B_8) > NON_CRIT_B && fabs(B - B_9) > NON_CRIT_B
		//	&& fabs(B - B_10) > NON_CRIT_B) 
		//		
		if(i!=0)
		{
			M_B = (F_B[i - 1] - F_B[i]) / B_DELTA;
			printf("%lf %.16e\n", B, M_B);
			fprintf(stderr, "%lf %.16e\n", B, muMin);
		}
	}
}

int main(int argc, char* argv[])
{
#ifdef __GNUC__
	int rank, procs;
	StartMpi(argc, argv, &procs, &rank);
	uint64_t lower = rank * (B_TOTAL / procs);
	uint64_t upper = lower + (B_TOTAL / procs);
	DinamicMu_M_B1(lower,upper);
	FinishMpi();
#else
	DinamicMu_M_B1(0, B_TOTAL);
#endif
	return 0;
}
