#include <stdint.h> // int types
#include <stdlib.h> // calloc, abs
#include <math.h>	// sqrt, exp
#include <stdio.h>	// printf

#ifdef __GNUC__ // For real MPI check need other predefined variables. For compilation under windows OS only.
	#include <mpi.h>	// MPI support (Open MPI)
#endif

#ifdef __GNUC__
	#define INLINE __attribute__((always_inline)) inline
#else
	#define INLINE inline
#endif

#define _PI (3.14159265358979323846) // To avoid conflicts.
#define E_POINTS (100000)  // NUM of E points
#define MU_POINTS (50000) // NUM of MU points
#define LANDAU_TOTAL_LEVELS (25) // Total Landau levels
#define LIGHT_SPEED (29979245800) // Light speed cm/sec
#define N_KV (3000000000) // sm^-2  - plotnost' dyrok - kolixhestvo dyrok na edinicu ploschsdi (v 1 sm^2)
#define ELECTRON_MASS (9.10938356E-28) // Electron mass in gramm.
#define PLANK_CONSTANT (6.626070040E-27) // Plank's constant erg s.
#define ELECTRON_CRARGE (4.803204673E-10) // Electron charge (Fr).
#define BOLTZMANN_CONSTANT (1.38064852E-16) // Boltzman constant erg/k.
#define M_C ((2E-4) * ELECTRON_MASS) // Effective mass - effektivnaya massa elektrona - vhodit v formuly vmesto obychnoi
#define H_RED (PLANK_CONSTANT / (2 * _PI)) // (Red corner??? - eto chto takoe vy napisali??)  Plank's constant reduc. - eto postoyannaya Planka s chertoi (reducirovannaya)
#define K_SGS_MEV (1.6E-15) // erg delim na koefficient - poluchaem milielektron-Volt 
#define T (300) // Temperature. U nas komnatnaya, 23 gradusa po Celsiyu = 300 Kelvinov
#define STRUCT_SQUARE (0.94E-2) // cm^2 - ploschad' nashei struktury
#define HOLES_NUM (N_KV * STRUCT_SQUARE) // Holes number in the structure - polnoe kolichestvo dyrok
#define MU_HOLES_ACTION_SI (200) //  m^2/s V Hole action in SI
#define MU_POD (MU_HOLES_ACTION_SI * 3E+6) // podvizhnost' dyrok v SGS - nuzhno dlya drugoi formuly dlya shiriny Г, poka ne nuzhno
#define K_GAMMA ((H_RED * ELECTRON_CRARGE / M_C) * sqrt(2 / (_PI * MU_POD * LIGHT_SPEED))) //  koefficient v shirine urovnei Ando (drugaya formula dlya shiriny Г)
#define K_GAMMA_MEV (K_GAMMA / K_SGS_MEV) // koefficient v shirine urovnei v meV
#define B_MIN (120)
#define B_MAX (140)
#define B_DELTA (0.0125)
#define B_TOTAL ((uint64_t)((B_MAX - B_MIN) / B_DELTA))
#define B_1 (1241)
#define B_2 (620)
#define B_3 (414)
#define B_4 (310)
#define B_5 (248)
#define B_6 (207)
#define B_7 (177)
#define B_8 (155)
#define B_9 (138)
#define B_10 (124)
#define NON_CRIT_B (B_DELTA)
//
//
#define LANDAU(B) ((ELECTRON_CRARGE * B * STRUCT_SQUARE) / (PLANK_CONSTANT * LIGHT_SPEED)) // vyrozhdenie urovnei Landau (ne dvumernoe!, obychnoe)
#define LOOP_FR(B) ((ELECTRON_CRARGE * B) / (LIGHT_SPEED * M_C)) // cyklotronnaya chastota
#define GAMMA_WEIGHT(B) (0.08 * sqrt(B)) // // shirina urovnya Gamma, 0.08 meV !!
#define D(E,Ej,Gamma) ((1/(Gamma*sqrt(2*_PI)))*exp(-(((E-Ej)*(E-Ej))/(2*Gamma*Gamma)))) // eto plotnost' sostouanii D
#define FERMI_DIARAK(E, Mu) (1/(1+exp((E-Mu)/(T*BOLTZMANN_CONSTANT))))
//
//
INLINE int GetLandauLevel(const double B)
{
	if (B > B_1) return 1;
	else if (B <= B_1 && B > B_2) return 2;
	else if (B <= B_2 && B > B_3) return 3;
	else if (B <= B_3 && B > B_4) return 4;
	else if (B <= B_4 && B > B_5) return 5;
	else if (B <= B_5 && B > B_6) return 6;
	else if (B <= B_6 && B > B_7) return 7;
	else if (B <= B_7 && B > B_8) return 8;
	else if (B <= B_8 && B > B_9) return 9;
	else if (B <= B_9 && B > B_10) return 10;
	else  return 11;
}

INLINE void Integrate(const int j, double* const target, const double delta)
{
	if (j == 0 || j == E_POINTS) {
		*target = *target + (delta / 3.0);
	}
	else if (j % 2 == 1) {
		*target = *target + (delta * 4.0) / 3.0;
	}
	else {
		*target = *target + (delta * 2.0) / 3.0;
	}
}
#ifdef __GNUC__
	INLINE void StartMpi(int argc, char* argv[], int* const procs, int* const rank)
	{
		MPI_Init(&argc, &argv);
		MPI_Comm_size(MPI_COMM_WORLD, procs);
		MPI_Comm_rank(MPI_COMM_WORLD, rank);
	}

	INLINE void FinishMpi()
	{
		MPI_Finalize();
	}
#endif

