#include <stdint.h> //int types
#include <stdlib.h> //calloc, abs
#include <math.h> //sqrt, exp
#include <stdio.h> //printf
#include <mpi.h>

#define M_PI 3.14159265358979323846
#define OUTPUT_FILE "D:\\test.txt"

static void DinamicMu_M_B1(int argc, char* argv[]) {
  
  int rank, procs;

  MPI_Init(&argc, &argv);
  MPI_Comm_size(MPI_COMM_WORLD, &procs);
  MPI_Comm_rank(MPI_COMM_WORLD, &rank);

  const double n_kv = 3E+9; //sm^2
  const double m_e = 9.10938356E-28; //g
  const double m_c = 2E-4 * m_e;
  const double c = 29979245800; //sm/s
  const double h = 6.626070040E-27; //erg s
  const double e = 4.803204673E-10; //Fr
  const double k = 1.38064852E-16; //erg/K
  const double h_red = h / (2 * M_PI);
  const double koef_SGS_meV = 1.6E-15; // erg delim na koefficient - poluchaem milielektron-Volt
  const double T = 10.0; //!!
  const double Sstruct = 0.94E-2; //sm^2
  const double Nstruct = n_kv * Sstruct; // kolichestvo dyrok v strukture
  const double mu_pod_SI = 200; // m^2 /s V // podvizhnost' dyrok v SI
  const double mu_pod = mu_pod_SI * 3E+6;
  const double koefGamma = (h_red * e / m_c) * sqrt(2 / (M_PI * mu_pod * c)); // koefficient v shirine urovnei Ando
  const double koefGamma_meV = koefGamma / koef_SGS_meV; // // koefficient v shirine urovnei v meV
                               //System.out.println("koefGamma_meV= "+ koefGamma_meV);
                               //------------------------------------------------
  const double B_1 = 1241;
  const double B_2 = 620;
  const double B_3 = 414;
  const double B_4 = 310;
  const double B_5 = 248;
  const double B_6 = 207;
  const double B_7 = 177;
  const double B_8 = 155;
  const double B_9 = 138;
  const double B_10 = 124;
  const double B_min = 120;
  const double B_max = 1320;
  const double deltaB = 0.5; // shag izmeneniya B
  const uint32_t numberB = (uint32_t)((B_max - B_min) / deltaB); // kolichestvo tochek B
  const double non_crit_B = deltaB; // dlya izbavleniya ot NaN

  const uint32_t numberE = 50; //kolichestvo tochek energii E
  const uint32_t numberMu = 500; // kolichestvo tochek himicheskogo potenciala mu

                //double[] E_B = new double[numberB]; // polnaya energiya
  double* M_B = (double*)calloc(numberB, sizeof(double)); // magnitnyi moment
  double* F_B = (double*)calloc(numberB, sizeof(double)); // termodinamicheskii potencial (Omega u Lifshica-Kosevicha)
  double* nkv_B = (double*)calloc(numberB, sizeof(double)); // chislo chastic (ne plotnost'!) kak funkciya B
  double* mu_B = (double*)calloc(numberB, sizeof(double)); // himicheskii potencial kak funkciya B

  const uint32_t totalNumberLandauLevel = 25; //min=11 !! // kolichestvo urobnei Landau
  double* E_F = (double*)calloc(totalNumberLandauLevel, sizeof(double)); // energiya Fermi po staticheskoi formule
  double* E_j = (double*)calloc(totalNumberLandauLevel, sizeof(double)); // energiya urovnei Landau
  double* E_F_meV = (double*)calloc(totalNumberLandauLevel, sizeof(double)); // // energiya Fermi po staticheskoi formule v meV
  double* E_j_meV = (double*)calloc(totalNumberLandauLevel, sizeof(double)); // energiya urovnei Landau v meV

  int lower = rank*(numberB/procs);
  int upper = (lower+(numberB/procs));

  FILE* ff;
  if ((ff = fopen(OUTPUT_FILE, "w+")) != NULL)
  { 
    // cykl po B
    for (int i = lower; i < upper; i++) {

      //int i = 100; // dlya otladki - dlya odnogo zhacheniya B
      double B = B_min + i * deltaB;
      //B = 608D;
      //System.out.println("B= " + B);

      double g = e * B * Sstruct / (h * c); // vyrozhdenie urovnei Landau (ne dvumernoe!, obychnoe)
      double omega_c = e * B / (c * m_c); // cyklotronnaya chastota

      int n_f = 0; // nomer urovnya Landau


      if (B > B_1) {
        n_f = 1;
      }
      else if (B <= B_1 && B > B_2) {
        n_f = 2;
      }
      else if (B <= B_2 && B > B_3) {
        n_f = 3;
      }
      else if (B <= B_3 && B > B_4) {
        n_f = 4;
      }
      else if (B <= B_4 && B > B_5) {
        n_f = 5;
      }
      else if (B <= B_5 && B > B_6) {
        n_f = 6;
      }
      else if (B <= B_6 && B > B_7) {
        n_f = 7;
      }
      else if (B <= B_7 && B > B_8) {
        n_f = 8;
      }
      else if (B <= B_8 && B > B_9) {
        n_f = 9;
      }
      else if (B <= B_9 && B > B_10) {
        n_f = 10;
      }
      else if (B <= B_10) {
        n_f = 11;
      }
      else {
        n_f = 11;
      }

      n_f = n_f - 1; //!

      for (int ik = 0; ik <= totalNumberLandauLevel - 1; ik++) {
        E_F[ik] = h_red * omega_c * (2 * ik + 1) / 2;
        E_F_meV[ik] = h_red * omega_c * (2 * ik + 1) / (2 * koef_SGS_meV);
        //System.out.println("B= " + B + " n_f= " + n_f + " " + ik + " E_F= " + E_F[ik]);
      }

      const double mu_min = E_F[n_f] * 0.6; // minimal'noe znachenie diapazona mu
      const double mu_max = E_F[n_f] * 1.4; // maximal'noe znachenie diapazona mu
      double muMin = mu_min; // mu, realizuyuschee minimal'noe otklonenie ot Nstruct
      double diffMin = 1.0E+5; // minimal'noe otklonenie chisla chastic ot Nstruct
      int jjmin = 0; // kogda dostigaetsya nuzhnoe mu


      for (int ik = 0; ik <= totalNumberLandauLevel - 1; ik++) {
        E_j[ik] = h_red * omega_c * (ik + 0.5);
        E_j_meV[ik] = h_red * omega_c * (ik + 0.5) / koef_SGS_meV;
      }

      double Gamma = 0.08 * sqrt(B); // shirina urovnya Gamma, 0.08 meV !!
                        //double Gamma = koefGamma_meV * Math.sqrt(B);

      double E_max = E_j[totalNumberLandauLevel - 1] + h_red * omega_c; // makcimal'noe znachenie energii E
      double E_max_meV = (E_j[totalNumberLandauLevel - 1] + h_red * omega_c) / koef_SGS_meV; // makcimal'noe znachenie energii E v meV
                                                   //System.out.println("E_max_meV= " + E_max_meV);

      double E_min = 0.0; // minimal'noe znachenie energii E
      double E_min_meV = 0.0;

      double Ncount = -1.0; // poschitannoe chislo chastic

                   // cykl po mu
      for (int jj = 0; jj <= numberMu; jj++) {
        double mu = mu_min + jj * (mu_max - mu_min) / numberMu;

        double nkv_full = 0.0; // tekuschee chislo chastic pri integrirovanii

                    // cykl po energii
        for (int j = 0; j <= numberE; j++) {

          double En = E_min + j * (E_max - E_min) / numberE; // tekuschee znachenie energii

          double f_FD = 1.0 / (1 + exp((En - mu) / (k * T))); // funkciya Fermi-Diraka
                                        //pw.printf("%E %f%n", En, f_FD); //

          double En_meV = En / koef_SGS_meV; // tekuschee znachenie energii v meV

          double D_en = 0.0;
          double D_en_meV = 0.0;
          for (int ikk = 0; ikk <= totalNumberLandauLevel - 1; ikk++) { //!
            D_en = D_en + exp(-(En - E_j[ikk]) * (En - E_j[ikk]) / (2 * Gamma * Gamma)) / (sqrt(2 * M_PI) * Gamma);
            D_en_meV = D_en_meV + exp(-(En_meV - E_j_meV[ikk]) * (En_meV - E_j_meV[ikk]) / (2 * Gamma * Gamma)) / (sqrt(2 * M_PI) * Gamma);
            //pw.printf("%d %E %d %E%n", j, En, ikk, D_en); //
          }
          double podint_nkv = g * D_en_meV * f_FD / koef_SGS_meV; // podintegral'naya funkciya v integrale dlya Nstruct
                                      //System.out.println("podint_nkv= " + podint_nkv);

                                      // integrirovanie metodom Simpsona
          if (j == 0 || j == numberE) {
            nkv_full = nkv_full + podint_nkv / 3.0;
          }
          else if (j % 2 == 1) {
            nkv_full = nkv_full + podint_nkv * 4.0 / 3.0;
          }
          else {
            nkv_full = nkv_full + podint_nkv * 2.0 / 3.0;
          }

        } // konec cykla po E
        nkv_full = nkv_full * (E_max - E_min) / numberE;

        double diff = fabs(nkv_full - Nstruct);
        //System.out.println(jj + " " + diff);
        if (fabs(diff) < fabs(diffMin)) {
          diffMin = diff;
          muMin = mu;
          Ncount = nkv_full;
          jjmin = jj;
        }
      } // konec cykla po mu

      mu_B[i] = muMin;
      //System.out.println(B + " " + diffMin);
      //System.out.println(B + " " + Ncount);
      //System.out.println(B + " " + mu_B[i] + " " + Ncount + " " + jjmin + " " + diffMin);

      //                if (i != 0 && Math.abs(B - B_1) > non_crit_B && Math.abs(B - B_2) > non_crit_B && Math.abs(B - B_3) > non_crit_B
      //                        && Math.abs(B - B_4) > non_crit_B && Math.abs(B - B_5) > non_crit_B && Math.abs(B - B_6) > non_crit_B
      //                        && Math.abs(B - B_7) > non_crit_B && Math.abs(B - B_8) > non_crit_B && Math.abs(B - B_9) > non_crit_B
      //                        && Math.abs(B - B_10) > non_crit_B) {
      //
      //                    pw.printf("%f %E%n", B, mu_B[i]);
      //                }

      double F_full = 0.0; // tekuschii termodinamicheskii potencial pri integrirovanii

                  // a teper' s dannym himicheskim potencialom schtitaem termodinamicheskii potencial
                  // vtoroi cykl po energii
      for (int j = 0; j <= numberE; j++) {

        double En = E_min + j * (E_max - E_min) / numberE;

        double f_FD = 1.0 / (1 + exp((En - mu_B[i]) / (k * T)));
        //pw.printf("%E %f%n", En, f_FD); //

        double En_meV = En / koef_SGS_meV; // !! meV !!!!!!!!!!!!!!!!!!!!

        double D_en = 0.0;
        double D_en_meV = 0.0;
        for (int ikk = 0; ikk <= totalNumberLandauLevel - 1; ikk++) { //!
          D_en = D_en + exp(-(En - E_j[ikk]) * (En - E_j[ikk]) / (2 * Gamma * Gamma)) / (sqrt(2 * M_PI) * Gamma);
          D_en_meV = D_en_meV + exp(-(En_meV - E_j_meV[ikk]) * (En_meV - E_j_meV[ikk]) / (2 * Gamma * Gamma)) / (sqrt(2 * M_PI) * Gamma);
          //pw.printf("%d %E %d %E%n", j, En, ikk, D_en); //
        }
        double podint_F = -g * k * T * D_en_meV * log(1.0 + exp((-En + mu_B[i]) / (k * T))) / koef_SGS_meV;

        // integrirovanie metodom Simpsona
        if (j == 0 || j == numberE) {
          F_full = F_full + podint_F / 3.0;
        }
        else if (j % 2 == 1) {
          F_full = F_full + podint_F * 4.0 / 3.0;
        }
        else {
          F_full = F_full + podint_F * 2.0 / 3.0;
        }
      } // konec vtorogo cikla po energii

      F_full = F_full * (E_max - E_min) / numberE;
      F_B[i] = F_full;


      if (i != 0) {
        M_B[i] = (F_B[i - 1] - F_B[i]) / deltaB;
      }
      printf("%lf %.16e\n",B, M_B[i]);


      if (i != 0 && fabs(B - B_1) > non_crit_B && fabs(B - B_2) > non_crit_B && fabs(B - B_3) > non_crit_B
        && fabs(B - B_4) > non_crit_B && fabs(B - B_5) > non_crit_B && fabs(B - B_6) > non_crit_B
        && fabs(B - B_7) > non_crit_B && fabs(B - B_8) > non_crit_B && fabs(B - B_9) > non_crit_B
        && fabs(B - B_10) > non_crit_B) {

        fprintf(ff, "%lf %.16e\n", B, M_B[i]);
        //System.out.println(B + " " + M_B[i]);
      }

    } // konec cykla po B!!
    fclose(ff);
  }
  else {
    printf("I/O FILE OPEN ERROR!");
  }
  MPI_Finalize();
}


int main(int argc, char* argv[])
{
  DinamicMu_M_B1(argc,argv);
  return 0;
}
