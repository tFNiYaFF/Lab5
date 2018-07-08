
import java.io.*;
import java.util.*;
import static java.lang.Math.*;

public class mag {

    public static void main(String[] args) {


        //System.out.println("Magnetic");
       // Date date1 = new Date();
       // System.out.println("Begin: " + date1.toString());

        //StaticMu_M_B();
        DinamicMu_M_B1();
        //Pila();

        //Vagner_B();
        //DinamicMu_M_B2();
       // Lif();


       // Date date2 = new Date();
       // System.out.println("End  : " + date2.toString());

    }

    public static void StaticMu_M_B() {
        final double n_kv = 3E+9D; //sm^2
        final double m_e = 9.10938356E-28D; //g
        final double m_c = 2E-4D * m_e;
        final double c = 29979245800D; //sm/s
        final double h = 6.626070040E-27D; //erg s
        final double e = 4.803204673E-10D; //Fr
        final double k = 1.38064852E-16D; //erg/K
        final double h_red = h / (2 * PI);
        final double magneton_bora_zv = e * h_red / (2 * c * m_c);

        final double n_kv_SI = 3E+13D; //m^2
        final double m_e_SI = 9.10938356E-31D; //kg
        final double m_c_SI = 2E-4D * m_e_SI;
        final double c_SI = 299792458D; //m/s
        final double h_SI = 6.626070040E-34D; //Dzh s
        final double e_SI = 1.602176565E-19D; //Kl
        final double k_SI = 1.38064852E-23D; //Dzh/K
        final double h_red_SI = h_SI / (2 * PI);
        final double magneton_bora_zv_SI = e_SI * h_red_SI / (2 * m_c_SI);

        final double PI = 3.1415926535897932D;

        final double T = 300.0D; //!!

        final double Sobr = 4E-8D; //sm^2
        final double Spodl = 3E-1D; //sm^2
        final double Sstruct = 0.94E-2D; //sm^2

        final double Spodl_SI = 3E-5D; //m^2
        final double Sstruct_SI = 0.94E-6D; //m^2

        final double phi0 = h * c / e;
        final double d_z = 1E-10D; //!!!

        final double rho = 2.33D; //!!! g/sm^3
        final double rho_SI = 2.33E+3D; //!!! kg/m^3

        final double mu_pod_SI = 200; // m^2 /s V
        final double mu_pod = mu_pod_SI * 3E+6D;

        final double D0_SI = m_c_SI / (PI * h_red_SI * h_red_SI);

        final double koef_SGS_meV = 1.6E-15D; // erg delim na koefficient - poluchaem milielektron-Volt

        final double koefGamma = (h_red * e / m_c) * Math.sqrt(2 / (Math.PI * mu_pod * c) ); // koefficient v shirine urovnei
        final double koefGamma_meV = koefGamma / koef_SGS_meV;
        //System.out.println("koefGamma_meV= "+ koefGamma_meV);

        final int number_of_B_zv = 10;

        final double B_1 = 1241;
        final double B_2 = 620;
        final double B_3 = 414;
        final double B_4 = 310;
        final double B_5 = 248;
        final double B_6 = 207;
        final double B_7 = 177;
        final double B_8 = 155;
        final double B_9 = 138;
        final double B_10 = 124;


        final double B_min = 120;
        final double B_max = 1320;
        final double deltaB = 2; //
        final int numberB = (int) ((B_max - B_min) / deltaB);
        final double non_crit_B = deltaB * 1.1;

        final int numberE = 100000;

        double[] E_B = new double[numberB];
        double[] M_B = new double[numberB];
        double[] F_B = new double[numberB];
        double[] nkv_B = new double[numberB];

        final int totalNumberLandauLevel = 25; //min=11 !!
        double[] E_F = new double[totalNumberLandauLevel];
        double[] E_j = new double[totalNumberLandauLevel];
        double[] E_F_meV = new double[totalNumberLandauLevel];
        double[] E_j_meV = new double[totalNumberLandauLevel];

        File ff = new File("C:\\test.txt");
        FileWriter fw = null;
        BufferedWriter bw = null;
        PrintWriter pw = null;

        try {
            fw = new FileWriter(ff, true);
            bw = new BufferedWriter(fw);
            pw = new PrintWriter(bw);

            for (int i = 0; i < numberB; i++) {

                //int i = 10000; //!!!!!!!!!!!!!!!!!!
                double B = B_min + i * deltaB;
                //System.out.println("B= " + B);

                double g = e * B * Sstruct / (h * c);
                double omega_c = e * B / (c * m_c);
                int n_f = 0;


                if (B > B_1) {
                    n_f = 1;
                } else if (B <= B_1 && B > B_2) {
                    n_f = 2;
                } else if (B <= B_2 && B > B_3) {
                    n_f = 3;
                } else if (B <= B_3 && B > B_4) {
                    n_f = 4;
                } else if (B <= B_4 && B > B_5) {
                    n_f = 5;
                } else if (B <= B_5 && B > B_6) {
                    n_f = 6;
                } else if (B <= B_6 && B > B_7) {
                    n_f = 7;
                } else if (B <= B_7 && B > B_8) {
                    n_f = 8;
                } else if (B <= B_8 && B > B_9) {
                    n_f = 9;
                } else if (B <= B_9 && B > B_10) {
                    n_f = 10;
                } else if (B <= B_10) {
                    n_f = 11;
                } else {
                    n_f = 11;
                }


                n_f = n_f - 1; //!


                for (int ik = 0; ik <= totalNumberLandauLevel - 1; ik++) {
                    E_F[ik] = h_red * omega_c * (2 * ik + 1) / 2;
                    E_F_meV[ik] = h_red * omega_c * (2 * ik + 1) / (2 * koef_SGS_meV);
                    //System.out.println("B= " + B + " n_f= " + n_f + " " + ik + " E_F= " + E_F[ik]);
                }


                for (int ik = 0; ik <= totalNumberLandauLevel - 1; ik++) {
                    E_j[ik] = h_red * omega_c * (ik + 0.5D);
                    E_j_meV[ik] = h_red * omega_c * (ik + 0.5D) / koef_SGS_meV;
                }

                double Gamma = 0.08 * Math.sqrt(B); //!
                //double Gamma = 1.0E-3D; //!
                //double Gamma = 0.08 * B; //!
                //double Gamma = 0.08 * Math.pow(B, 0.75D); //!
                //double Gamma = koefGamma_meV * Math.sqrt(B);

                double E_max = E_j[totalNumberLandauLevel - 1] + h_red * omega_c; //!
                double E_max_meV = (E_j[totalNumberLandauLevel - 1] + h_red * omega_c) / koef_SGS_meV; //!

                double E_min = 0.D;
                double E_min_meV = 0.D;

                double E_full = 0.0D;
                double E_full_meV = 0.0D;

                double F_full = 0.0D;
                double nkv_full = 0.0D;



                for (int j = 0; j <= numberE; j++) {

                    double En = E_min + j * (E_max - E_min) / numberE;

                    double f_FD = 1.0D / (1 + Math.exp((En - E_F[n_f]) / (k * T)));
                    //pw.printf("%E %f%n", En, f_FD); //

                    double En_meV = En / koef_SGS_meV; // !! meV !!!!!!!!!!!!!!!!!!!!

                    double D_en = 0.0D;
                    double D_en_meV = 0.0D;
                    for (int ikk =  0; ikk <= totalNumberLandauLevel - 1; ikk++) { //!
                    //for (int ikk = 0; ikk <= n_f; ikk++) { //!
                        D_en = D_en + Math.exp(-(En - E_j[ikk]) * (En - E_j[ikk]) / (2 * Gamma * Gamma )) / (Math.sqrt(2 * PI) * Gamma);
                        D_en_meV = D_en_meV + Math.exp(-(En_meV - E_j_meV[ikk]) * (En_meV - E_j_meV[ikk]) / (2 * Gamma * Gamma )) / (Math.sqrt(2 * PI) * Gamma);
                        //pw.printf("%d %E %d %E%n", j, En, ikk, D_en); //
                    }
                    //pw.printf("%E %E %E%n", En_meV, D_en_meV, f_FD); //
                    //double podint = g * En * D_en * f_FD;
                    //double podint_meV = g * En_meV * D_en_meV * f_FD;
                    //double podint_F = -g * k * T * D_en_meV * Math.log(1.0D + Math.exp((-En + E_F[n_f]) / (k * T))) / koef_SGS_meV ;
                    double podint_nkv = g * D_en_meV * f_FD / koef_SGS_meV;

                    //pw.printf("%f %E%n", En_meV, podint_meV); //
                    //pw.printf("%E %E %E%n", En,  (-En + E_F[n_f]) / (k * T), Math.exp((-En + E_F[n_f]) / (k * T))); //
                    //pw.printf("%f %d %f %E %E %E %E %n", B, j, g, En, D_en, f_FD, podint); //
                    //pw.printf("%E %E%n", En_meV, podint_F);


                    if (j == 0 || j == numberE) {
                        //E_full = E_full + podint / 3.0D;
                        //E_full_meV = E_full_meV + podint_meV / 3.0D;
                        //F_full = F_full + podint_F / 3.0D;
                        nkv_full = nkv_full + podint_nkv / 3.0D;
                    } else if (j % 2 == 1) {
                        //E_full = E_full + podint * 4.0D / 3.0D;
                        //E_full_meV = E_full_meV + podint_meV * 4.0D / 3.0D;
                        //F_full = F_full + podint_F * 4.0D / 3.0D;
                        nkv_full = nkv_full + podint_nkv * 4.0D / 3.0D;
                    } else {
                        //E_full = E_full + podint * 2.0D / 3.0D;
                        //E_full_meV = E_full_meV + podint_meV * 2.0D / 3.0D;
                        //F_full = F_full + podint_F * 2.0D / 3.0D;
                        nkv_full = nkv_full + podint_nkv * 2.0D / 3.0D;
                    }
                }

                //E_full = E_full * (E_max - E_min) / numberE; //erg
                //E_full_meV = E_full_meV * (E_max_meV - E_min_meV) / numberE;
                //F_full = F_full * (E_max - E_min) / numberE;
                nkv_full = nkv_full * (E_max - E_min) / numberE;
                //E_B[i] = E_full ;
                //E_B[i] = E_full_meV; //meV
                //F_B[i] = F_full;
                nkv_B[i] = nkv_full;

                if (i != 0) {
                    //M_B[i] = (E_B[i - 1] - E_B[i]) * koef_SGS_meV / deltaB;
                    //M_B[i] = (F_B[i - 1] - F_B[i])  / deltaB;
                }
                //System.out.println(B + " " + M_B[i]);


                if (i != 0 && Math.abs(B - B_1) > non_crit_B && Math.abs(B - B_2) > non_crit_B && Math.abs(B - B_3) > non_crit_B
                        && Math.abs(B - B_4) > non_crit_B && Math.abs(B - B_5) > non_crit_B && Math.abs(B - B_6) > non_crit_B
                        && Math.abs(B - B_7) > non_crit_B && Math.abs(B - B_8) > non_crit_B && Math.abs(B - B_9) > non_crit_B
                        && Math.abs(B - B_10) > non_crit_B) {

                    //System.out.println(B + " " + E_F[n_f]);
                    //pw.printf("%f %E%n", B, M_B[i]);
                     //pw.printf("%f %E%n", B, E_B[i]); //
                    //pw.printf("%f %d %E %E%n", B, i, E_B[i], M_B[i]); //
                    pw.printf("%f %E%n", B, nkv_B[i]);
                }
            } //!! B-cycle
            pw.close();
        } catch (IOException ee) {
            System.err.println("ошибка открытия потока " + ee);
        } finally {
            pw.close();
        }

        Date date2 = new Date();
        System.out.println("End:   " + date2.toString());

    }

    public static void DinamicMu_M_B1() {

        // Konstanty ----------------------------------
        final double n_kv = 3E+9D; //sm^2
        final double m_e = 9.10938356E-28D; //g
        final double m_c = 2E-4D * m_e;
        final double c = 29979245800D; //sm/s
        final double h = 6.626070040E-27D; //erg s
        final double e = 4.803204673E-10D; //Fr
        final double k = 1.38064852E-16D; //erg/K
        final double h_red = h / (2 * Math.PI);
        final double koef_SGS_meV = 1.6E-15D; // erg delim na koefficient - poluchaem milielektron-Volt
        //final double PI = 3.1415926535897932D;
        final double T = 10.0D; //!!

        final double Sstruct = 0.94E-2D; //sm^2
        final double Nstruct = n_kv * Sstruct; // kolichestvo dyrok v strukture
        final double mu_pod_SI = 200; // m^2 /s V // podvizhnost' dyrok v SI
        final double mu_pod = mu_pod_SI * 3E+6D;

        final double koefGamma = (h_red * e / m_c) * Math.sqrt(2 / (Math.PI * mu_pod * c)); // koefficient v shirine urovnei Ando
        final double koefGamma_meV = koefGamma / koef_SGS_meV; // // koefficient v shirine urovnei v meV
        //System.out.println("koefGamma_meV= "+ koefGamma_meV);
        //------------------------------------------------

        final double B_1 = 1241;
        final double B_2 = 620;
        final double B_3 = 414;
        final double B_4 = 310;
        final double B_5 = 248;
        final double B_6 = 207;
        final double B_7 = 177;
        final double B_8 = 155;
        final double B_9 = 138;
        final double B_10 = 124;

        final double B_min = 120;
        final double B_max = 1320;
        final double deltaB = 0.5; // shag izmeneniya B
        final int numberB = (int) ((B_max - B_min) / deltaB); // kolichestvo tochek B
        final double non_crit_B = deltaB; // dlya izbavleniya ot NaN

        final int numberE = 50; //kolichestvo tochek energii E
        final int numberMu = 500; // kolichestvo tochek himicheskogo potenciala mu

        //double[] E_B = new double[numberB]; // polnaya energiya
        double[] M_B = new double[numberB]; // magnitnyi moment
        double[] F_B = new double[numberB]; // termodinamicheskii potencial (Omega u Lifshica-Kosevicha)
        double[] nkv_B = new double[numberB]; // chislo chastic (ne plotnost'!) kak funkciya B
        double[] mu_B = new double[numberB]; // himicheskii potencial kak funkciya B

        final int totalNumberLandauLevel = 25; //min=11 !! // kolichestvo urobnei Landau
        double[] E_F = new double[totalNumberLandauLevel]; // energiya Fermi po staticheskoi formule
        double[] E_j = new double[totalNumberLandauLevel]; // energiya urovnei Landau
        double[] E_F_meV = new double[totalNumberLandauLevel]; // // energiya Fermi po staticheskoi formule v meV
        double[] E_j_meV = new double[totalNumberLandauLevel]; // energiya urovnei Landau v meV

        File ff = new File("D:\\test.txt");
        FileWriter fw = null;
        BufferedWriter bw = null;
        PrintWriter pw = null;

        try {
            fw = new FileWriter(ff, true);
            bw = new BufferedWriter(fw);
            pw = new PrintWriter(bw);

            // cykl po B
            for (int i = 0; i < numberB; i++) {

                //int i = 100; // dlya otladki - dlya odnogo zhacheniya B
                double B = B_min + i * deltaB;
                //B = 608D;
                //System.out.println("B= " + B);

                double g = e * B * Sstruct / (h * c); // vyrozhdenie urovnei Landau (ne dvumernoe!, obychnoe)
                double omega_c = e * B / (c * m_c); // cyklotronnaya chastota

                int n_f = 0; // nomer urovnya Landau


                if (B > B_1) {
                    n_f = 1;
                } else if (B <= B_1 && B > B_2) {
                    n_f = 2;
                } else if (B <= B_2 && B > B_3) {
                    n_f = 3;
                } else if (B <= B_3 && B > B_4) {
                    n_f = 4;
                } else if (B <= B_4 && B > B_5) {
                    n_f = 5;
                } else if (B <= B_5 && B > B_6) {
                    n_f = 6;
                } else if (B <= B_6 && B > B_7) {
                    n_f = 7;
                } else if (B <= B_7 && B > B_8) {
                    n_f = 8;
                } else if (B <= B_8 && B > B_9) {
                    n_f = 9;
                } else if (B <= B_9 && B > B_10) {
                    n_f = 10;
                } else if (B <= B_10) {
                    n_f = 11;
                } else {
                    n_f = 11;
                }

                n_f = n_f - 1; //!

                for (int ik = 0; ik <= totalNumberLandauLevel - 1; ik++) {
                    E_F[ik] = h_red * omega_c * (2 * ik + 1) / 2;
                    E_F_meV[ik] = h_red * omega_c * (2 * ik + 1) / (2 * koef_SGS_meV);
                    //System.out.println("B= " + B + " n_f= " + n_f + " " + ik + " E_F= " + E_F[ik]);
                }

                final double mu_min = E_F[n_f] * 0.6; // minimal'noe znachenie diapazona mu
                final double mu_max = E_F[n_f] * 1.4; // maximal'noe znachenie diapazona mu
                double muMin = mu_min; // mu, realizuyuschee minimal'noe otklonenie ot Nstruct
                double diffMin = 1.0E+5D; // minimal'noe otklonenie chisla chastic ot Nstruct
                int jjmin = 0; // kogda dostigaetsya nuzhnoe mu


                for (int ik = 0; ik <= totalNumberLandauLevel - 1; ik++) {
                    E_j[ik] = h_red * omega_c * (ik + 0.5D);
                    E_j_meV[ik] = h_red * omega_c * (ik + 0.5D) / koef_SGS_meV;
                }

                double Gamma = 0.08 * Math.sqrt(B); // shirina urovnya Gamma, 0.08 meV !!
                //double Gamma = koefGamma_meV * Math.sqrt(B);

                double E_max = E_j[totalNumberLandauLevel - 1] + h_red * omega_c; // makcimal'noe znachenie energii E
                double E_max_meV = (E_j[totalNumberLandauLevel - 1] + h_red * omega_c) / koef_SGS_meV; // makcimal'noe znachenie energii E v meV
                //System.out.println("E_max_meV= " + E_max_meV);

                double E_min = 0.D; // minimal'noe znachenie energii E
                double E_min_meV = 0.D;

                double Ncount = -1.0D; // poschitannoe chislo chastic

                // cykl po mu
                for (int jj = 0; jj <= numberMu; jj++) {
                    double mu = mu_min + jj * (mu_max - mu_min) / numberMu;

                    double nkv_full = 0.0D; // tekuschee chislo chastic pri integrirovanii

                    // cykl po energii
                    for (int j = 0; j <= numberE; j++) {

                        double En = E_min + j * (E_max - E_min) / numberE; // tekuschee znachenie energii

                        double f_FD = 1.0D / (1 + Math.exp((En - mu) / (k * T))); // funkciya Fermi-Diraka
                        //pw.printf("%E %f%n", En, f_FD); //

                        double En_meV = En / koef_SGS_meV; // tekuschee znachenie energii v meV

                        double D_en = 0.0D;
                        double D_en_meV = 0.0D;
                        for (int ikk = 0; ikk <= totalNumberLandauLevel - 1; ikk++) { //!
                            D_en = D_en + Math.exp(-(En - E_j[ikk]) * (En - E_j[ikk]) / (2 * Gamma * Gamma)) / (Math.sqrt(2 * Math.PI) * Gamma);
                            D_en_meV = D_en_meV + Math.exp(-(En_meV - E_j_meV[ikk]) * (En_meV - E_j_meV[ikk]) / (2 * Gamma * Gamma)) / (Math.sqrt(2 * Math.PI) * Gamma);
                            //pw.printf("%d %E %d %E%n", j, En, ikk, D_en); //
                        }
                        double podint_nkv = g * D_en_meV * f_FD / koef_SGS_meV; // podintegral'naya funkciya v integrale dlya Nstruct
                        //System.out.println("podint_nkv= " + podint_nkv);

                        // integrirovanie metodom Simpsona
                        if (j == 0 || j == numberE) {
                            nkv_full = nkv_full + podint_nkv / 3.0D;
                        } else if (j % 2 == 1) {
                            nkv_full = nkv_full + podint_nkv * 4.0D / 3.0D;
                        } else {
                            nkv_full = nkv_full + podint_nkv * 2.0D / 3.0D;
                        }

                    } // konec cykla po E
                    nkv_full = nkv_full * (E_max - E_min) / numberE;

                    double diff = Math.abs(nkv_full - Nstruct);
                    //System.out.println(jj + " " + diff);
                    if (Math.abs(diff) < Math.abs(diffMin)) {
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

                double F_full = 0.0D; // tekuschii termodinamicheskii potencial pri integrirovanii

                // a teper' s dannym himicheskim potencialom schtitaem termodinamicheskii potencial
                // vtoroi cykl po energii
                for (int j = 0; j <= numberE; j++) {

                    double En = E_min + j * (E_max - E_min) / numberE;

                    double f_FD = 1.0D / (1 + Math.exp((En - mu_B[i]) / (k * T)));
                    //pw.printf("%E %f%n", En, f_FD); //

                    double En_meV = En / koef_SGS_meV; // !! meV !!!!!!!!!!!!!!!!!!!!

                    double D_en = 0.0D;
                    double D_en_meV = 0.0D;
                    for (int ikk =  0; ikk <= totalNumberLandauLevel - 1; ikk++) { //!
                        D_en = D_en + Math.exp(-(En - E_j[ikk]) * (En - E_j[ikk]) / (2 * Gamma * Gamma )) / (Math.sqrt(2 * PI) * Gamma);
                        D_en_meV = D_en_meV + Math.exp(-(En_meV - E_j_meV[ikk]) * (En_meV - E_j_meV[ikk]) / (2 * Gamma * Gamma )) / (Math.sqrt(2 * PI) * Gamma);
                        //pw.printf("%d %E %d %E%n", j, En, ikk, D_en); //
                    }
                    double podint_F = -g * k * T * D_en_meV * Math.log(1.0D + Math.exp((-En + mu_B[i]) / (k * T))) / koef_SGS_meV ;

                    // integrirovanie metodom Simpsona
                    if (j == 0 || j == numberE) {
                        F_full = F_full + podint_F / 3.0D;
                    } else if (j % 2 == 1) {
                        F_full = F_full + podint_F * 4.0D / 3.0D;
                    } else {
                        F_full = F_full + podint_F * 2.0D / 3.0D;
                    }
                } // konec vtorogo cikla po energii

                F_full = F_full * (E_max - E_min) / numberE;
                F_B[i] = F_full;


                if (i != 0) {
                    M_B[i] = (F_B[i - 1] - F_B[i])  / deltaB;
                }
                System.out.println(B + " " + M_B[i]);


                if (i != 0 && Math.abs(B - B_1) > non_crit_B && Math.abs(B - B_2) > non_crit_B && Math.abs(B - B_3) > non_crit_B
                        && Math.abs(B - B_4) > non_crit_B && Math.abs(B - B_5) > non_crit_B && Math.abs(B - B_6) > non_crit_B
                        && Math.abs(B - B_7) > non_crit_B && Math.abs(B - B_8) > non_crit_B && Math.abs(B - B_9) > non_crit_B
                        && Math.abs(B - B_10) > non_crit_B) {

                    pw.printf("%f %E%n", B, M_B[i]);
                    //System.out.println(B + " " + M_B[i]);
                }

            } // konec cykla po B!!

            pw.close();
        } catch (IOException ee) {
            System.err.println("ошибка открытия потока " + ee);
        } finally {
            pw.close();
        }
    }

    public static void DinamicMu_M_B() {


        final double n_kv = 3E+9D;
        final double m_e = 9.10938356E-28D;
        final double m_c = 2E-4D * m_e;
        final double c = 29979245800D;
        final double h = 6.626070040E-27D;
        final double e = 4.803204673E-10D;
        final double k = 1.38064852E-16D;
        final double h_red = h / (2 * Math.PI);
        final double koef_SGS_meV = 1.6E-15D;

        final double T = 300.0D; //!!

        final double Sstruct = 0.94E-2D;
        final double Nstruct = n_kv * Sstruct;
        final double mu_pod_SI = 200;
        final double mu_pod = mu_pod_SI * 3E+6D;

        final double koefGamma = (h_red * e / m_c) * Math.sqrt(2 / (Math.PI * mu_pod * c));
        final double koefGamma_meV = koefGamma / koef_SGS_meV;


        final double B_1 = 1241;
        final double B_2 = 620;
        final double B_3 = 414;
        final double B_4 = 310;
        final double B_5 = 248;
        final double B_6 = 207;
        final double B_7 = 177;
        final double B_8 = 155;
        final double B_9 = 138;
        final double B_10 = 124;

        final double B_min = 120;
        final double B_max = 1320;
        final double deltaB = 0.5;
        final int numberB = (int) ((B_max - B_min) / deltaB);
        final double non_crit_B = deltaB;

        final int numberE = 100000;
        final int numberMu = 100000;


        double[] M_B = new double[numberB];
        double[] F_B = new double[numberB];
        double[] nkv_B = new double[numberB];
        double[] mu_B = new double[numberB];

        final int totalNumberLandauLevel = 25;
        double[] E_F = new double[totalNumberLandauLevel];
        double[] E_j = new double[totalNumberLandauLevel];
        double[] E_F_meV = new double[totalNumberLandauLevel];
        double[] E_j_meV = new double[totalNumberLandauLevel];

        File ff = new File("C:\\test.txt");
        FileWriter fw = null;
        BufferedWriter bw = null;
        PrintWriter pw = null;

        try {
            fw = new FileWriter(ff, true);
            bw = new BufferedWriter(fw);
            pw = new PrintWriter(bw);


            for (int i = 0; i < numberB; i++) {


                double B = B_min + i * deltaB;


                double g = e * B * Sstruct / (h * c);
                double omega_c = e * B / (c * m_c);

                int n_f = 0;


                if (B > B_1) {
                    n_f = 1;
                } else if (B <= B_1 && B > B_2) {
                    n_f = 2;
                } else if (B <= B_2 && B > B_3) {
                    n_f = 3;
                } else if (B <= B_3 && B > B_4) {
                    n_f = 4;
                } else if (B <= B_4 && B > B_5) {
                    n_f = 5;
                } else if (B <= B_5 && B > B_6) {
                    n_f = 6;
                } else if (B <= B_6 && B > B_7) {
                    n_f = 7;
                } else if (B <= B_7 && B > B_8) {
                    n_f = 8;
                } else if (B <= B_8 && B > B_9) {
                    n_f = 9;
                } else if (B <= B_9 && B > B_10) {
                    n_f = 10;
                } else if (B <= B_10) {
                    n_f = 11;
                } else {
                    n_f = 11;
                }

                n_f = n_f - 1; //!

                for (int ik = 0; ik <= totalNumberLandauLevel - 1; ik++) {
                    E_F[ik] = h_red * omega_c * (2 * ik + 1) / 2;
                    E_F_meV[ik] = h_red * omega_c * (2 * ik + 1) / (2 * koef_SGS_meV);
                    //System.out.println("B= " + B + " n_f= " + n_f + " " + ik + " E_F= " + E_F[ik]);
                }

                final double mu_min = E_F[n_f] * 0.6;
                final double mu_max = E_F[n_f] * 1.4;
                double muMin = mu_min;
                double diffMin = 1.0E+5D;
                int jjmin = 0;


                for (int ik = 0; ik <= totalNumberLandauLevel - 1; ik++) {
                    E_j[ik] = h_red * omega_c * (ik + 0.5D);
                    E_j_meV[ik] = h_red * omega_c * (ik + 0.5D) / koef_SGS_meV;
                }


                double Gamma = koefGamma_meV * Math.sqrt(B);

                double E_max = E_j[totalNumberLandauLevel - 1] + h_red * omega_c;
                double E_max_meV = (E_j[totalNumberLandauLevel - 1] + h_red * omega_c) / koef_SGS_meV;
                //System.out.println("E_max_meV= " + E_max_meV);

                double E_min = 0.D;
                double E_min_meV = 0.D;

                double Ncount = -1.0D;


                for (int jj = 0; jj <= numberMu; jj++) {
                    double mu = mu_min + jj * (mu_max - mu_min) / numberMu;

                    double nkv_full = 0.0D;


                    for (int j = 0; j <= numberE; j++) {

                        double En = E_min + j * (E_max - E_min) / numberE;

                        double f_FD = 1.0D / (1 + Math.exp((En - mu) / (k * T)));
                        //pw.printf("%E %f%n", En, f_FD); //

                        double En_meV = En / koef_SGS_meV;

                        double D_en = 0.0D;
                        double D_en_meV = 0.0D;
                        for (int ikk = 0; ikk <= totalNumberLandauLevel - 1; ikk++) {
                            D_en = D_en + Math.exp(-(En - E_j[ikk]) * (En - E_j[ikk]) / (2 * Gamma * Gamma)) / (Math.sqrt(2 * Math.PI) * Gamma);
                            D_en_meV = D_en_meV + Math.exp(-(En_meV - E_j_meV[ikk]) * (En_meV - E_j_meV[ikk]) / (2 * Gamma * Gamma)) / (Math.sqrt(2 * Math.PI) * Gamma);
                            //pw.printf("%d %E %d %E%n", j, En, ikk, D_en); //
                        }
                        double podint_nkv = g * D_en_meV * f_FD / koef_SGS_meV;
                        //System.out.println("podint_nkv= " + podint_nkv);


                        if (j == 0 || j == numberE) {
                            nkv_full = nkv_full + podint_nkv / 3.0D;
                        } else if (j % 2 == 1) {
                            nkv_full = nkv_full + podint_nkv * 4.0D / 3.0D;
                        } else {
                            nkv_full = nkv_full + podint_nkv * 2.0D / 3.0D;
                        }

                    }
                    nkv_full = nkv_full * (E_max - E_min) / numberE;

                    double diff = Math.abs(nkv_full - Nstruct);
                    //System.out.println(jj + " " + diff);
                    if (Math.abs(diff) < Math.abs(diffMin)) {
                        diffMin = diff;
                        muMin = mu;
                        Ncount = nkv_full;
                        jjmin = jj;
                    }

                }

                mu_B[i] = muMin;
                //System.out.println(B + " " + diffMin);
                //System.out.println(B + " " + Ncount);
                //System.out.println(B + " " + mu_B[i] + " " + Ncount + " " + jjmin + " " + diffMin);

                double F_full = 0.0D;


                for (int j = 0; j <= numberE; j++) {

                    double En = E_min + j * (E_max - E_min) / numberE;

                    double f_FD = 1.0D / (1 + Math.exp((En - mu_B[i]) / (k * T)));
                    //pw.printf("%E %f%n", En, f_FD); //

                    double En_meV = En / koef_SGS_meV;

                    double D_en = 0.0D;
                    double D_en_meV = 0.0D;
                    for (int ikk =  0; ikk <= totalNumberLandauLevel - 1; ikk++) {
                        D_en = D_en + Math.exp(-(En - E_j[ikk]) * (En - E_j[ikk]) / (2 * Gamma * Gamma )) / (Math.sqrt(2 * PI) * Gamma);
                        D_en_meV = D_en_meV + Math.exp(-(En_meV - E_j_meV[ikk]) * (En_meV - E_j_meV[ikk]) / (2 * Gamma * Gamma )) / (Math.sqrt(2 * PI) * Gamma);
                        //pw.printf("%d %E %d %E%n", j, En, ikk, D_en); //
                    }
                    double podint_F = -g * k * T * D_en_meV * Math.log(1.0D + Math.exp((-En + mu_B[i]) / (k * T))) / koef_SGS_meV ;


                    if (j == 0 || j == numberE) {
                        F_full = F_full + podint_F / 3.0D;
                    } else if (j % 2 == 1) {
                        F_full = F_full + podint_F * 4.0D / 3.0D;
                    } else {
                        F_full = F_full + podint_F * 2.0D / 3.0D;
                    }
                }

                F_full = F_full * (E_max - E_min) / numberE;
                F_B[i] = F_full;


                if (i != 0) {
                    M_B[i] = (F_B[i - 1] - F_B[i])  / deltaB;
                }
                System.out.println(B + " " + M_B[i]);


                if (i != 0 && Math.abs(B - B_1) > non_crit_B && Math.abs(B - B_2) > non_crit_B && Math.abs(B - B_3) > non_crit_B
                        && Math.abs(B - B_4) > non_crit_B && Math.abs(B - B_5) > non_crit_B && Math.abs(B - B_6) > non_crit_B
                        && Math.abs(B - B_7) > non_crit_B && Math.abs(B - B_8) > non_crit_B && Math.abs(B - B_9) > non_crit_B
                        && Math.abs(B - B_10) > non_crit_B) {

                    pw.printf("%f %E%n", B, M_B[i]);
                    //System.out.println(B + " " + M_B[i]);
                }

            }

            pw.close();
        } catch (IOException ee) {
            System.err.println("ошибка открытия потока " + ee);
        } finally {
            pw.close();
        }
    }

    public static void Efull_B() {
        final double n_kv = 3E+9D; //sm^2
        final double m_e = 9.10938356E-28D; //g
        final double m_c = 2E-4D * m_e;
        final double c = 29979245800D; //sm/s
        final double h = 6.626070040E-27D; //erg s
        final double e = 4.803204673E-10D; //Fr
        final double k = 1.38064852E-16D; //erg/K
        final double h_red = h / (2 * PI);
        final double magneton_bora_zv = e * h_red / (2 * c * m_c);

        final double n_kv_SI = 3E+13D; //m^2
        final double m_e_SI = 9.10938356E-31D; //kg
        final double m_c_SI = 2E-4D * m_e_SI;
        final double c_SI = 299792458D; //m/s
        final double h_SI = 6.626070040E-34D; //Dzh s
        final double e_SI = 1.602176565E-19D; //Kl
        final double k_SI = 1.38064852E-23D; //Dzh/K
        final double h_red_SI = h_SI / (2 * PI);
        final double magneton_bora_zv_SI = e_SI * h_red_SI / (2 * m_c_SI);

        final double PI = 3.1415926535897932D;

        final double T = 300.0D; //!!

        final double Sobr = 4E-8D; //sm^2
        final double Spodl = 3E-1D; //sm^2
        final double Sstruct = 0.94E-2D; //sm^2

        final double d_podl = 35E-3D; //sm //tolschina podlozhki

        final double Spodl_SI = 3E-5D; //m^2
        final double Sstruct_SI = 0.94E-6D; //m^2

        final double phi0 = h * c / e;
        final double d_z = 1E-10D; //!!!

        final double rho = 2.33D; //!!! g/sm^3
        final double rho_SI = 2.33E+3D; //!!! kg/m^3

        final double mu_pod_SI = 200; // m^2 /s V
        final double mu_pod = mu_pod_SI * 3E+6D;

        final double D0_SI = m_c_SI / (PI * h_red_SI * h_red_SI);

        final double koef_SGS_meV = 1.6E-15D; // erg delim na koefficient - poluchaem milielektron-Volt

        final double koefGamma = (h_red * e / m_c) * Math.sqrt(2 / (Math.PI * mu_pod * c) ); // koefficient v shirine urovnei
        final double koefGamma_meV = koefGamma / koef_SGS_meV;
        //System.out.println("koefGamma_meV= "+ koefGamma_meV);

        final int number_of_B_zv = 10;

        final double B_1 = 1241;
        final double B_2 = 620;
        final double B_3 = 414;
        final double B_4 = 310;
        final double B_5 = 248;
        final double B_6 = 207;
        final double B_7 = 177;
        final double B_8 = 155;
        final double B_9 = 138;
        final double B_10 = 124;


        final double B_min = 120;
        final double B_max = 1320;
        //final double B_max = 207;
        final double deltaB = 0.01; //
        final int numberB = (int) ((B_max - B_min) / deltaB);


        double[] E_B = new double[numberB];
        double[] M_B = new double[numberB];
        double[] I_B = new double[numberB];

        File ff = new File("C:\\Users\\Vadim\\Downloads\\12345.txt");
        FileWriter fw = null;
        BufferedWriter bw = null;
        PrintWriter pw = null;

        try {
            fw = new FileWriter(ff, true);
            bw = new BufferedWriter(fw);
            pw = new PrintWriter(bw);

            for (int i = 0; i < numberB; i++) {

                //int i = 10000; //!!!!!!!!!!!!!!!!!!
                double B = B_min + i * deltaB;
                //System.out.println("B= " + B);

                double g = e * B * Sstruct / (h * c); // s uchetom spina
                double g2 = 2 * g; // bez ucheta spina

                double omega_c = e * B / (c * m_c);
                int n_f = 0;


                if (B > B_1) {
                    n_f = 1;
                } else if (B <= B_1 && B > B_2) {
                    n_f = 2;
                } else if (B <= B_2 && B > B_3) {
                    n_f = 3;
                } else if (B <= B_3 && B > B_4) {
                    n_f = 4;
                } else if (B <= B_4 && B > B_5) {
                    n_f = 5;
                } else if (B <= B_5 && B > B_6) {
                    n_f = 6;
                } else if (B <= B_6 && B > B_7) {
                    n_f = 7;
                } else if (B <= B_7 && B > B_8) {
                    n_f = 8;
                } else if (B <= B_8 && B > B_9) {
                    n_f = 9;
                } else if (B <= B_9 && B > B_10) {
                    n_f = 10;
                } else if (B <= B_10) {
                    n_f = 11;
                } else {
                    n_f = 11;
                }


                n_f = n_f - 1; //!!!!!!!!!!!!!!!!!

                if (B > B_1) {
                    //E_B[i] = n_kv * Sstruct * magneton_bora_zv * B;
                    M_B[i] = - n_kv * Sstruct * magneton_bora_zv;
                    I_B[i] = M_B[i] / (Spodl * d_podl);
                } else {
                    //E_B[i] = (2 * n_f + 1) * n_kv * Sstruct * magneton_bora_zv * B - n_f * (n_f + 1) * magneton_bora_zv * g * B;
                    M_B[i] = - (2 * n_f + 1) * n_kv * Sstruct * magneton_bora_zv  + 2 * n_f * (n_f + 1) * magneton_bora_zv * g;
                    I_B[i] = M_B[i] / (Spodl * d_podl);
                }

                //double H_kr = n_kv *  c * h / (e * (n_f+1));

                //System.out.println("B= " + B + " H_kr= " + H_kr);

                pw.printf("%f %E%n", B, I_B[i]);


                }

            pw.close();
        } catch (IOException ee) {
            System.err.println("ошибка открытия потока " + ee);
        } finally {
            pw.close();
        }

        Date date2 = new Date();
        System.out.println("End:   " + date2.toString());

    }

    public static void Pila() {
        final double n_kv = 3E+9D; //sm^2
        final double m_e = 9.10938356E-28D; //g
        final double m_c = 2E-4D * m_e;
        final double c = 29979245800D; //sm/s
        final double h = 6.626070040E-27D; //erg s
        final double e = 4.803204673E-10D; //Fr
        final double k = 1.38064852E-16D; //erg/K
        final double h_red = h / (2 * PI);
        final double magneton_bora_zv = e * h_red / (2 * c * m_c);

        final double n_kv_SI = 3E+13D; //m^2
        final double m_e_SI = 9.10938356E-31D; //kg
        final double m_c_SI = 2E-4D * m_e_SI;
        final double c_SI = 299792458D; //m/s
        final double h_SI = 6.626070040E-34D; //Dzh s
        final double e_SI = 1.602176565E-19D; //Kl
        final double k_SI = 1.38064852E-23D; //Dzh/K
        final double h_red_SI = h_SI / (2 * PI);
        final double magneton_bora_zv_SI = e_SI * h_red_SI / (2 * m_c_SI);

        final double PI = 3.1415926535897932D;

        final double T = 300.0D; //!!

        final double Sobr = 4E-8D; //sm^2
        final double Spodl = 3E-1D; //sm^2
        final double Sstruct = 0.94E-2D; //sm^2

        final double d_podl = 35E-3D; //sm //tolschina podlozhki

        final double Spodl_SI = 3E-5D; //m^2
        final double Sstruct_SI = 0.94E-6D; //m^2

        final double phi0 = h * c / e;
        final double d_z = 1E-10D; //!!!

        final double rho = 2.33D; //!!! g/sm^3
        final double rho_SI = 2.33E+3D; //!!! kg/m^3

        final double mu_pod_SI = 200; // m^2 /s V
        final double mu_pod = mu_pod_SI * 3E+6D;

        final double D0_SI = m_c_SI / (PI * h_red_SI * h_red_SI);

        final double koef_SGS_meV = 1.6E-15D; // erg delim na koefficient - poluchaem milielektron-Volt

        final double koefGamma = (h_red * e / m_c) * Math.sqrt(2 / (Math.PI * mu_pod * c) ); // koefficient v shirine urovnei
        final double koefGamma_meV = koefGamma / koef_SGS_meV;
        //System.out.println("koefGamma_meV= "+ koefGamma_meV);

        final int number_of_B_zv = 10;

        final double B_1 = 1241;
        final double B_2 = 620;
        final double B_3 = 414;
        final double B_4 = 310;
        final double B_5 = 248;
        final double B_6 = 207;
        final double B_7 = 177;
        final double B_8 = 155;
        final double B_9 = 138;
        final double B_10 = 124;


        final double B_min = 120;
        final double B_max = 1320;
        //final double B_max = 207;
        final double deltaB = 0.01; //
        final int numberB = (int) ((B_max - B_min) / deltaB);


        double[] E_B = new double[numberB];
        double[] M_B = new double[numberB];
        double[] I_B = new double[numberB];

        File ff = new File("C:\\Users\\Vadim\\Downloads\\12345.txt");
        FileWriter fw = null;
        BufferedWriter bw = null;
        PrintWriter pw = null;

        try {
            fw = new FileWriter(ff, true);
            bw = new BufferedWriter(fw);
            pw = new PrintWriter(bw);

            final int B_pila_avg = 210;
            final double I_pila_avg = 0.47E-7D;
            final int B_Pila_max_init = 5125;
            final double I_Pila_max_init = 2.1E-7D;
            final double I_Pila_min_init = I_Pila_max_init - I_pila_avg;
            final int numberPila = (int) (B_Pila_max_init / B_pila_avg);

            double[] B_pila_max = new double [numberPila];
            double[] B_pila_min = new double [numberPila];
            for (int iP = 0; iP < numberPila; iP++) {
                B_pila_max[numberPila-iP-1] = B_Pila_max_init - B_pila_avg * iP;
                B_pila_min[numberPila-iP-1] = B_pila_max[numberPila-iP-1] - B_pila_avg / 2;
            }

            double[] koef_nakl1 = new double [numberPila];
            double[] koef_nakl2 = new double [numberPila - 1];
            double[] koef_b1 = new double [numberPila];
            double[] koef_b2 = new double [numberPila - 1];

            final int numberB_Pila = B_pila_avg; // shag v 1 Gs
            double B = 0;

            double[] I_Pila = new double [numberPila * numberB_Pila];

            for (int iP = 0; iP < numberPila; iP++){

                koef_nakl1[iP] = (I_Pila_max_init - I_Pila_min_init) / (B_pila_max[iP] - B_pila_min[iP]);
                koef_b1[iP] = I_Pila_max_init - koef_nakl1[iP] * B_pila_max[iP];
                if (iP != numberPila - 1) {
                    koef_nakl2[iP] = (I_Pila_max_init - I_Pila_min_init) / (B_pila_max[iP] - B_pila_min[iP + 1]);
                    koef_b2[iP] = I_Pila_max_init - koef_nakl2[iP] * B_pila_max[iP];
                }
                for (int iB = 0; iB < numberB_Pila; iB++) {
                    B = B_pila_min[iP] + iB;
                    if ((iB <= numberB_Pila / 2) && (iP != numberPila - 1)) {
                        I_Pila[iP * numberPila + iB] = koef_nakl1[iP] * B + koef_b1[iP];
                        //if (I_Pila[iP * numberPila + iB] > 0) pw.printf("%f %E%n", B, I_Pila[iP * numberPila + iB]);
                    }
                    else if (iP != numberPila - 1) {
                        I_Pila[iP * numberPila + iB] = koef_nakl2[iP] * B + koef_b2[iP];
                        //if (I_Pila[iP * numberPila + iB] > 0) pw.printf("%f %E%n", B, I_Pila[iP * numberPila + iB]);
                    }
                    if (iB % 5 == 0) pw.printf("%f %E%n", B, I_Pila[iP * numberPila + iB]);
                }

            }

/*
            for (int i = 0; i < numberB; i++) {

                //int i = 10000; //!!!!!!!!!!!!!!!!!!
                double B = B_min + i * deltaB;
                //System.out.println("B= " + B);

                double g = e * B * Sstruct / (h * c); // s uchetom spina
                double g2 = 2 * g; // bez ucheta spina

                double omega_c = e * B / (c * m_c);
                int n_f = 0;


                if (B > B_1) {
                    n_f = 1;
                } else if (B <= B_1 && B > B_2) {
                    n_f = 2;
                } else if (B <= B_2 && B > B_3) {
                    n_f = 3;
                } else if (B <= B_3 && B > B_4) {
                    n_f = 4;
                } else if (B <= B_4 && B > B_5) {
                    n_f = 5;
                } else if (B <= B_5 && B > B_6) {
                    n_f = 6;
                } else if (B <= B_6 && B > B_7) {
                    n_f = 7;
                } else if (B <= B_7 && B > B_8) {
                    n_f = 8;
                } else if (B <= B_8 && B > B_9) {
                    n_f = 9;
                } else if (B <= B_9 && B > B_10) {
                    n_f = 10;
                } else if (B <= B_10) {
                    n_f = 11;
                } else {
                    n_f = 11;
                }



                n_f = n_f - 1; //!!!!!!!!!!!!!!!!!

                if (B > B_1) {
                    //E_B[i] = n_kv * Sstruct * magneton_bora_zv * B;
                    M_B[i] = - n_kv * Sstruct * magneton_bora_zv;
                    I_B[i] = M_B[i] / (Spodl * d_podl);
                } else {
                    //E_B[i] = (2 * n_f + 1) * n_kv * Sstruct * magneton_bora_zv * B - n_f * (n_f + 1) * magneton_bora_zv * g * B;
                    M_B[i] = - (2 * n_f + 1) * n_kv * Sstruct * magneton_bora_zv  + 2 * n_f * (n_f + 1) * magneton_bora_zv * g;
                    I_B[i] = M_B[i] / (Spodl * d_podl);
                }

                //double H_kr = n_kv *  c * h / (e * (n_f+1));

                //System.out.println("B= " + B + " H_kr= " + H_kr);

                pw.printf("%f %E%n", B, I_B[i]);


            }
*/


            pw.close();
        } catch (IOException ee) {
            System.err.println("ошибка открытия потока " + ee);
        } finally {
            pw.close();
        }

        Date date2 = new Date();
        System.out.println("End:   " + date2.toString());

    }

    public static void Vagner_B() {
        final double n_kv = 3E+9D; //sm^2
        final double m_e = 9.10938356E-28D; //g
        final double m_c = 2E-4D * m_e;
        final double c = 29979245800D; //sm/s
        final double h = 6.626070040E-27D; //erg s
        final double e = 4.803204673E-10D; //Fr
        final double k = 1.38064852E-16D; //erg/K
        final double h_red = h / (2 * PI);
        final double magneton_bora_zv = e * h_red / (2 * c * m_c);

        final double n_kv_SI = 3E+13D; //m^2
        final double m_e_SI = 9.10938356E-31D; //kg
        final double m_c_SI = 2E-4D * m_e_SI;
        final double c_SI = 299792458D; //m/s
        final double h_SI = 6.626070040E-34D; //Dzh s
        final double e_SI = 1.602176565E-19D; //Kl
        final double k_SI = 1.38064852E-23D; //Dzh/K
        final double h_red_SI = h_SI / (2 * PI);
        final double magneton_bora_zv_SI = e_SI * h_red_SI / (2 * m_c_SI);

        final double PI = 3.1415926535897932D;

        final double T = 300.0D; //!!

        final double Sobr = 4E-8D; //sm^2
        final double Spodl = 3E-1D; //sm^2
        final double Sstruct = 0.94E-2D; //sm^2

        final double Spodl_SI = 3E-5D; //m^2
        final double Sstruct_SI = 0.94E-6D; //m^2

        final double phi0 = h * c / e;
        final double d_z = 1E-10D; //!!!

        final double rho = 2.33D; //!!! g/sm^3
        final double rho_SI = 2.33E+3D; //!!! kg/m^3

        final double mu_pod_SI = 200; // m^2 /s V
        final double mu_pod = mu_pod_SI * 3E+6D;

        final double D0_SI = m_c_SI / (PI * h_red_SI * h_red_SI);

        final double koef_SGS_meV = 1.6E-15D; // erg delim na koefficient - poluchaem milielektron-Volt

        final double koefGamma = (h_red * e / m_c) * Math.sqrt(2 / (Math.PI * mu_pod * c) ); // koefficient v shirine urovnei
        final double koefGamma_meV = koefGamma / koef_SGS_meV;
        //System.out.println("koefGamma_meV= "+ koefGamma_meV);

        final double koef_mass_struct = 3.6E-7D;
        final double D_0 = m_c * Sstruct / (PI * h_red * h_red);

        final int number_of_B_zv = 10;

        final double B_1 = 1241;
        final double B_2 = 620;
        final double B_3 = 414;
        final double B_4 = 310;
        final double B_5 = 248;
        final double B_6 = 207;
        final double B_7 = 177;
        final double B_8 = 155;
        final double B_9 = 138;
        final double B_10 = 124;


        final double B_min = 120;
        final double B_max = 1320;
        final double deltaB = 2; //
        final int numberB = (int) ((B_max - B_min) / deltaB);
        final double non_crit_B = deltaB * 1.1;


        final int numberBkr = 10;
        double[] D_loc_B = new double[numberBkr];


        double[] B_kr = new double[numberBkr];
        for (int i = 0; i < numberBkr; i++) {
            B_kr[i] = n_kv * c * h / (e * (i + 1));
            //System.out.println("B_kr= "+ B_kr[i]);
        }

        double[] hi_B = new double[numberBkr];
        // obychnaya
        hi_B[0] = 0.000000240; // 1241
        hi_B[1] = 0.000000772; // 620
        hi_B[2] = 0.000001774; // 414
        hi_B[3] = 0.000001990; // 310
        hi_B[4] = (0.000001722 + 0.000002096) / 2; //248
        hi_B[5] = 0.000002908; //207
        hi_B[6] = (0.000003980 + 0.000004143) / 2; //177
        hi_B[7] = 0.000003243; //155
        hi_B[8] = (0.000001757 + 0.000002386) / 2; // 138
        hi_B[9] = 0.000001137; // 124


        // differencialnaya:
        hi_B[0] = -0.000002408; // 1241
        hi_B[1] = 0.000000716; // 620
        hi_B[2] = 0.000001438; // 414
        hi_B[3] = -0.000000579; // 310
        hi_B[4] = (0.000002974 + 0.000003526) / 2; //248
        hi_B[5] = -0.000007566; //207
        hi_B[6] = (0.000006476 + 0.000003260) / 2; //177
        hi_B[7] = 0.000011794; //155
        hi_B[8] = (0.000000193 + 0.000004096) / 2; // 138
        hi_B[9] = -0.000007144;// 124


        for (int i = 0; i < numberBkr; i++) {
            hi_B[i] = hi_B[i] * rho / koef_mass_struct;
        }

        double[] n_f_kr = new double[numberBkr];
        for (int i = 0; i < numberBkr; i++) {
            n_f_kr[i] = i + 1;
        }

                File ff = new File("C:\\Users\\Vadim\\Downloads\\12347.txt");
        FileWriter fw = null;
        BufferedWriter bw = null;
        PrintWriter pw = null;

        try {
            fw = new FileWriter(ff, true);
            bw = new BufferedWriter(fw);
            pw = new PrintWriter(bw);

            for (int i = 0; i < numberBkr; i++) {

                double beta = e * Sstruct / (h * c);
                double omega_c = e * B_kr[i] / (c * m_c);
                double alpha = h_red * omega_c / (2 * k * T);


                D_loc_B[i] = n_kv * ((n_f_kr[i] - 1) * beta / (hi_B[i] * B_kr[i]) - 2 * Math.exp(- alpha) * Sstruct / (n_f_kr[i] * k * T)) ;
                //System.out.println(B_kr[i] + " " + D_loc_B[i]);
                //System.out.println(B_kr[i] + " " + D_loc_B[i]/D_0);
                System.out.println((i + 1) + " " + D_loc_B[i]/D_0);
                   // pw.printf("%f %E%n", B, nkv_B[i]);

            } //!! B-cycle
            pw.close();
        } catch (IOException ee) {
            System.err.println("ошибка открытия потока " + ee);
        } finally {
            pw.close();
        }

        Date date2 = new Date();
        System.out.println("End:   " + date2.toString());

    }

    public static void DinamicMu_M_B2() {


        final double n_kv = 3E+9D;
        final double m_e = 9.10938356E-28D;
        final double m_c = 2E-4D * m_e;
        final double c = 29979245800D;
        final double h = 6.626070040E-27D;
        final double e = 4.803204673E-10D;
        final double k = 1.38064852E-16D;
        final double h_red = h / (2 * Math.PI);
        final double koef_SGS_meV = 1.6E-15D;

        final double T = 300.0D; //!!

        final double Sstruct = 0.94E-2D;
        final double Nstruct = n_kv * Sstruct;
        System.out.println("Nstruct= " + Nstruct);
        final double mu_pod_SI = 200;
        final double mu_pod = mu_pod_SI * 3E+6D;

        final double koefGamma = (h_red * e / m_c) * Math.sqrt(2 / (Math.PI * mu_pod * c));
        final double koefGamma_meV = koefGamma / koef_SGS_meV;

        final double D_0 = m_c * Sstruct / (PI * h_red * h_red);
        System.out.println("D_0= " + D_0);

        final double B_1 = 1241;
        final double B_2 = 620;
        final double B_3 = 414;
        final double B_4 = 310;
        final double B_5 = 248;
        final double B_6 = 207;
        final double B_7 = 177;
        final double B_8 = 155;
        final double B_9 = 138;
        final double B_10 = 124;

        final double B_min = 120;
        final double B_max = 1320;
        final double deltaB = 0.5;
        final int numberB = (int) ((B_max - B_min) / deltaB);
        final double non_crit_B = deltaB;

        final int numberE = 100000;
        final int numberMu = 10000;


        double[] M_B = new double[numberB];
        double[] F_B = new double[numberB];
        double[] nkv_B = new double[numberB];
        double[] mu_B = new double[numberB];

        final int totalNumberLandauLevel = 25;
        double[] E_F = new double[totalNumberLandauLevel];
        double[] E_j = new double[totalNumberLandauLevel];
        double[] E_F_meV = new double[totalNumberLandauLevel];
        double[] E_j_meV = new double[totalNumberLandauLevel];

        File ff = new File("C:\\Users\\Vadim\\Downloads\\12347.txt");
        FileWriter fw = null;
        BufferedWriter bw = null;
        PrintWriter pw = null;

        try {
            fw = new FileWriter(ff, true);
            bw = new BufferedWriter(fw);
            pw = new PrintWriter(bw);


            for (int i = 0; i < numberB; i++) {


                double B = B_min + i * deltaB;
                B = B + 30.0; //!!

                double g = e * B * Sstruct / (h * c);
                double omega_c = e * B / (c * m_c);

                int n_f = 0;


                if (B > B_1) {
                    n_f = 1;
                } else if (B <= B_1 && B > B_2) {
                    n_f = 2;
                } else if (B <= B_2 && B > B_3) {
                    n_f = 3;
                } else if (B <= B_3 && B > B_4) {
                    n_f = 4;
                } else if (B <= B_4 && B > B_5) {
                    n_f = 5;
                } else if (B <= B_5 && B > B_6) {
                    n_f = 6;
                } else if (B <= B_6 && B > B_7) {
                    n_f = 7;
                } else if (B <= B_7 && B > B_8) {
                    n_f = 8;
                } else if (B <= B_8 && B > B_9) {
                    n_f = 9;
                } else if (B <= B_9 && B > B_10) {
                    n_f = 10;
                } else if (B <= B_10) {
                    n_f = 11;
                } else {
                    n_f = 11;
                }

                n_f = n_f - 1; //!

                for (int ik = 0; ik <= totalNumberLandauLevel - 1; ik++) {
                    E_F[ik] = h_red * omega_c * (2 * ik + 1) / 2;
                    E_F_meV[ik] = h_red * omega_c * (2 * ik + 1) / (2 * koef_SGS_meV);
                    //System.out.println("B= " + B + " n_f= " + n_f + " " + ik + " E_F= " + E_F[ik]);
                }

                final double mu_min = E_F[n_f] * 0.6;
                final double mu_max = E_F[n_f] * 1.4;
                double muMin = mu_min;
                double diffMin = 1.0E+5D;
                int jjmin = 0;


                for (int ik = 0; ik <= totalNumberLandauLevel - 1; ik++) {
                    E_j[ik] = h_red * omega_c * (ik + 0.5D);
                    E_j_meV[ik] = h_red * omega_c * (ik + 0.5D) / koef_SGS_meV;
                    //System.out.println("E_j[ik] = " + E_j[ik]);
                }


                double Gamma_new =  (E_j[2] -  E_j[1] ) / 6;
                double Gamma = koefGamma_meV * Math.sqrt(B);
                Gamma = koefGamma * Math.sqrt(B); // !!!
                //System.out.println("Gamma = " + Gamma);


                double E_max = E_j[totalNumberLandauLevel - 1] + h_red * omega_c;
                double E_max_meV = (E_j[totalNumberLandauLevel - 1] + h_red * omega_c) / koef_SGS_meV;
                //System.out.println("E_max_meV= " + E_max_meV);

                double E_min = 0.D;
                double E_min_meV = 0.D;

                double Ncount = -1.0D;


                for (int jj = 0; jj <= numberMu; jj++) {
                    double mu = mu_min + jj * (mu_max - mu_min) / numberMu;

                    double nkv_full = 0.0D;


                    for (int j = 0; j <= numberE; j++) {

                        double En = E_min + j * (E_max - E_min) / numberE;

                        double f_FD = 1.0D / (1 + Math.exp((En - mu) / (k * T)));
                        //pw.printf("%E %f%n", En, f_FD); //

                        double En_meV = En / koef_SGS_meV;

                        double D_en = 0.0D;
                        double D_en_meV = 0.0D;
                        for (int ikk = 0; ikk <= totalNumberLandauLevel - 1; ikk++) {
                            //D_en = D_en + Math.exp(-(En - E_j[ikk]) * (En - E_j[ikk]) / (2 * Gamma * Gamma)) / (Math.sqrt(2 * Math.PI) * Gamma);
                            //D_en_meV = D_en_meV + Math.exp(-(En_meV - E_j_meV[ikk]) * (En_meV - E_j_meV[ikk]) / (2 * Gamma * Gamma)) / (Math.sqrt(2 * Math.PI) * Gamma);

                            if (Math.abs(En - E_j[ikk]) < Gamma_new) {
                                D_en =  D_0;
                                //System.out.println(E_j[ikk] + " " + E_j[ikk + 1] + " " + Gamma / 5);
                                break;
                            }
                            else {
                                D_en = 0.45 * D_0;
                                //break;
                            }
                            //pw.printf("%d %E %d %E%n", j, En, ikk, D_en); //
                        }

                        //System.out.println(En + " " + D_en);
                        //double podint_nkv = g * D_en_meV * f_FD / koef_SGS_meV;
                        //double podint_nkv = g * D_en * f_FD;
                        double podint_nkv = D_en * f_FD;
                        //System.out.println("podint_nkv= " + podint_nkv);


                        if (j == 0 || j == numberE) {
                            nkv_full = nkv_full + podint_nkv / 3.0D;
                        } else if (j % 2 == 1) {
                            nkv_full = nkv_full + podint_nkv * 4.0D / 3.0D;
                        } else {
                            nkv_full = nkv_full + podint_nkv * 2.0D / 3.0D;
                        }

                    }
                    nkv_full = nkv_full * (E_max - E_min) / numberE;
                    //System.out.println(B + " " + nkv_full + " " + Nstruct);
                    double diff = Math.abs(nkv_full - Nstruct);
                    //System.out.println(jj + " " + diff);
                    if (Math.abs(diff) < Math.abs(diffMin)) {
                        diffMin = diff;
                        muMin = mu;
                        Ncount = nkv_full;
                        jjmin = jj;
                    }

                }


                mu_B[i] = muMin;


                //System.out.println(B + " " + diffMin);
                //System.out.println(B + " " + Ncount);
                //System.out.println(B + " " + mu_B[i] + " " + Ncount + " " + jjmin + " " + diffMin);
                System.out.println(B + " " + mu_B[i]);

                double F_full = 0.0D;


                for (int j = 0; j <= numberE; j++) {

                    double En = E_min + j * (E_max - E_min) / numberE;

                    double f_FD = 1.0D / (1 + Math.exp((En - mu_B[i]) / (k * T)));
                    //pw.printf("%E %f%n", En, f_FD); //

                    double En_meV = En / koef_SGS_meV;

                    double D_en = 0.0D;
                    double D_en_meV = 0.0D;
                    for (int ikk =  0; ikk <= totalNumberLandauLevel - 1; ikk++) {
                        //D_en = D_en + Math.exp(-(En - E_j[ikk]) * (En - E_j[ikk]) / (2 * Gamma * Gamma )) / (Math.sqrt(2 * PI) * Gamma);
                        //D_en_meV = D_en_meV + Math.exp(-(En_meV - E_j_meV[ikk]) * (En_meV - E_j_meV[ikk]) / (2 * Gamma * Gamma )) / (Math.sqrt(2 * PI) * Gamma);

                        if (Math.abs(En - E_j[ikk]) < Gamma / 4) {
                            D_en =  D_0;
                            break;
                        }
                        else {
                            D_en = 0.4 * D_0;
                            break;
                        }

                        //pw.printf("%d %E %d %E%n", j, En, ikk, D_en); //
                    }
                    //double podint_F = -g * k * T * D_en_meV * Math.log(1.0D + Math.exp((-En + mu_B[i]) / (k * T))) / koef_SGS_meV ;
                    double podint_F = - k * T * D_en * Math.log(1.0D + Math.exp((-En + mu_B[i]) / (k * T))) ;

                    if (j == 0 || j == numberE) {
                        F_full = F_full + podint_F / 3.0D;
                    } else if (j % 2 == 1) {
                        F_full = F_full + podint_F * 4.0D / 3.0D;
                    } else {
                        F_full = F_full + podint_F * 2.0D / 3.0D;
                    }
                }

                F_full = F_full * (E_max - E_min) / numberE;
                F_B[i] = F_full;

                //System.out.println(B + " " + F_B[i]);

                if (i != 0) {
                    M_B[i] = (F_B[i - 1] - F_B[i])  / deltaB;
                }
                //System.out.println(B + " " + M_B[i]);

/*
                if (i != 0 && Math.abs(B - B_1) > non_crit_B && Math.abs(B - B_2) > non_crit_B && Math.abs(B - B_3) > non_crit_B
                        && Math.abs(B - B_4) > non_crit_B && Math.abs(B - B_5) > non_crit_B && Math.abs(B - B_6) > non_crit_B
                        && Math.abs(B - B_7) > non_crit_B && Math.abs(B - B_8) > non_crit_B && Math.abs(B - B_9) > non_crit_B
                        && Math.abs(B - B_10) > non_crit_B)
*/
                {
                    pw.printf("%f %E%n", B, M_B[i]);
                   // System.out.println(B + " " + M_B[i]);
                }

            }

            pw.close();
        } catch (IOException ee) {
            System.err.println("ошибка открытия потока " + ee);
        } finally {
            pw.close();
        }
    }

    public static void Lif() {


        final double n_kv = 3E+9D;
        final double m_e = 9.10938356E-28D;
        //final double m_c = 2E-4D * m_e;
        final double m_c = 1E-7D * m_e; //
        //final double m_c = 2E-5D * m_e; //!!!!!!!!!
        final double c = 29979245800D;
        final double h = 6.626070040E-27D;
        final double e = 4.803204673E-10D;
        final double k = 1.38064852E-16D;
        final double h_red = h / (2 * Math.PI);
        final double koef_SGS_meV = 1.6E-15D;

        final double T = 300D; //!!

        final double Sstruct = 0.94E-2D;
        final double Nstruct = n_kv * Sstruct;
        System.out.println("Nstruct= " + Nstruct);
        final double mu_pod_SI = 200;
        final double mu_pod = mu_pod_SI * 3E+6D;

        final double rho = 2.33D; //!!! g/sm^3
        final double d_str = 2.0E-7D;

        final double koefGamma = (h_red * e / m_c) * Math.sqrt(2 / (Math.PI * mu_pod * c));
        final double koefGamma_meV = koefGamma / koef_SGS_meV;

        final double D_0 = m_c * Sstruct / (PI * h_red * h_red);
        //System.out.println("D_0= " + D_0);

        final double delta_1_B = 8.06E-4D;
        final double A_0 = 2 * Math.PI * e / (c * h_red * delta_1_B);
        final double A_0_sht_sht = Math.PI;

        final int numberBkr = 50;
        double[] B_kr = new double[numberBkr];
        for (int i = 0; i < numberBkr; i++) {
            B_kr[i] = n_kv * c * h / (e * (i + 1));
            //System.out.println(i + ", B_kr= "+ B_kr[i]);
        }


        final double B_1 = B_kr[0];
        final double B_2 = B_kr[1];
        final double B_3 = B_kr[2];
        final double B_4 = B_kr[3];
        final double B_5 = B_kr[4];
        final double B_6 = B_kr[5];
        final double B_7 = B_kr[6];
        final double B_8 = B_kr[7];
        final double B_9 = B_kr[8];
        final double B_10 = B_kr[9];

        final double B_11 = B_kr[10];
        final double B_12 = B_kr[11];
        final double B_13 = B_kr[12];
        final double B_14 = B_kr[13];
        final double B_15 = B_kr[14];
        final double B_16 = B_kr[15];
        final double B_17 = B_kr[16];
        final double B_18 = B_kr[17];
        final double B_19 = B_kr[18];
        final double B_20 = B_kr[19];


        final double B_min = 60;
        final double B_max = 1320;
        final double deltaB = 0.5;
        final int numberB = (int) ((B_max - B_min) / deltaB);
        final double non_crit_B = deltaB;

        final int numberS = 100;


        double[] M_B = new double[numberB];
        double[] F_B = new double[numberB];
        double[] I_ud_B = new double[numberB];
        double[] nkv_B = new double[numberB];
        double[] mu_B = new double[numberB];

        final int totalNumberLandauLevel = 25;
        double[] E_F = new double[totalNumberLandauLevel];
        double[] E_j = new double[totalNumberLandauLevel];
        double[] E_F_meV = new double[totalNumberLandauLevel];
        double[] E_j_meV = new double[totalNumberLandauLevel];

        File ff = new File("C:\\Users\\Vadim\\Downloads\\12347.txt");
        FileWriter fw = null;
        BufferedWriter bw = null;
        PrintWriter pw = null;

        try {
            fw = new FileWriter(ff, true);
            bw = new BufferedWriter(fw);
            pw = new PrintWriter(bw);


            for (int i = 0; i < numberB; i++) {


                double B = B_min + i * deltaB;
                //B = B + 2490; //!!


                double g = e * B * Sstruct / (h * c);
                double omega_c = e * B / (c * m_c);

                //double m_krs =   m_c * (1.0 + (20 * Math.pow(B,1.5D) / Math.pow(1000.0D,1.5D)));
                //double m_krs =   m_c * (1.0 + B / 50.D);
                //double m_krs =   m_c * (1.0 + (39 * B * Math.sqrt(B) / (1000 * Math.sqrt(1000))));
                //double m_krs =   m_c * (1.0 + (24 * Math.pow(B,1.25D) / Math.pow(1000,1.25D)));
                //double m_krs =   m_c * (1.0 + (19 * Math.pow(B,1.2D) / Math.pow(1000,1.2D)));

                //double m_krs =   m_c * (1.0 + (39 * B * Math.sqrt(B) / (1000 * Math.sqrt(1000))));
                //double m_krs =   m_c * (1.0 + (1 * Math.tanh(B * B / 50000(1.0 + (19 * Math.pow(B,1.2D) / Math.pow(1000,1.2D)))0)));

                 //double m_krs =   m_c * (1.0 + (4 * Math.pow(B,1.25D) / Math.pow(1000.0D,1.25D)));
                //double m_krs =   m_c * (1.0 + (7 * B * Math.sqrt(B) / (1000 * Math.sqrt(1000))));
                //double m_krs =   m_c * (1.0 + (7 * B * B * B / 1000000000));
                double m_krs =   m_c * (1.0 + (99 * B * B / 1000000));
                omega_c = e * B / (c * m_krs);


                int n_f = 0;


                if (B > B_1) {
                    n_f = 1;
                } else if (B <= B_1 && B > B_2) {
                    n_f = 2;
                } else if (B <= B_2 && B > B_3) {
                    n_f = 3;
                } else if (B <= B_3 && B > B_4) {
                    n_f = 4;
                } else if (B <= B_4 && B > B_5) {
                    n_f = 5;
                } else if (B <= B_5 && B > B_6) {
                    n_f = 6;
                } else if (B <= B_6 && B > B_7) {
                    n_f = 7;
                } else if (B <= B_7 && B > B_8) {
                    n_f = 8;
                } else if (B <= B_8 && B > B_9) {
                    n_f = 9;
                } else if (B <= B_9 && B > B_10) {
                    n_f = 10;
                } else if (B <= B_10 && B > B_11) {
                    n_f = 11;

                } else if (B <= B_11 && B > B_12) {
                    n_f = 12;
                } else if (B <= B_12 && B > B_13) {
                    n_f = 13;
                } else if (B <= B_13 && B > B_14) {
                    n_f = 14;
                } else if (B <= B_14 && B > B_15) {
                    n_f = 15;
                } else if (B <= B_15 && B > B_16) {
                    n_f = 16;
                } else if (B <= B_16 && B > B_17) {
                    n_f = 17;
                } else if (B <= B_17 && B > B_18) {
                    n_f = 18;
                } else if (B <= B_18 && B > B_19) {
                    n_f = 19;
                } else if (B <= B_19 && B > B_20) {
                    n_f = 20;
                } else if (B <= B_20) {
                    n_f = 21;




                } else {
                    n_f = 11;
                }

                n_f = n_f - 1; //!

                F_B[i] = 0.0;
                M_B[i] = 0.0;

                //omega_c = e * B / (c * m_e); //!!!

                for (int s = 0; s < numberS; s++) {
                    s++;

                    /*
                    F_B[i] = F_B[i] + 2 * k * T * Math.pow(-1,s) * Math.pow(e * B / (2 * Math.PI * s * c * h_red), 1.5) * Math.cos(s * c * h_red * A_0 / (e * B) - Math.PI / 4) /
                            (Math.sqrt(A_0_sht_sht) * Math.sinh(2 * Math.PI * Math.PI * s * k * T /(h_red * omega_c)));
                    */

                    M_B[i] = M_B[i] + 2 * k * T * Math.pow(-1,s+1) * Math.pow(e / (2 * Math.PI * s * c * h_red), 1.5) * Math.cos(s * c * h_red * A_0 / (e * B) - Math.PI / 4) /
                            (Math.sqrt(A_0_sht_sht) * Math.sinh(2 * Math.PI * Math.PI * s * k * T /(h_red * omega_c)) * Math.sqrt(B)) *
                            ( 1.5D * B + 2 * Math.PI *  Math.PI * s * k * T * c * m_c  / (Math.tanh(2 * Math.PI * Math.PI * s * k * T /(h_red * omega_c)) * h_red * e)
                            + s * c * h_red * A_0 * Math.tan(s * c * h_red * A_0 / (e * B) - Math.PI / 4) / e )  ; // eto M




                    //System.out.println(B + " " + (2 * Math.PI * Math.PI * s * k * T /(h_red * omega_c)));

                }
                //System.out.println(B + " " + F_B[i]);

                //I_ud_B[i] = M_B[i] / (Sstruct * d_str * rho); //
                I_ud_B[i] = M_B[i] /  rho; //
                //I_ud_B[i] = M_B[i] * 124 /  rho; // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

               /*
                if (i != 0) {
                    M_B[i] = (F_B[i - 1] - F_B[i])  / deltaB;
                    I_ud_B[i] = M_B[i] / (Sstruct * d_str * rho); //
                }
                */
                //System.out.println(B + " " + M_B[i]);

/*
                if (i != 0 && Math.abs(B - B_1) > non_crit_B && Math.abs(B - B_2) > non_crit_B && Math.abs(B - B_3) > non_crit_B
                        && Math.abs(B - B_4) > non_crit_B && Math.abs(B - B_5) > non_crit_B && Math.abs(B - B_6) > non_crit_B
                        && Math.abs(B - B_7) > non_crit_B && Math.abs(B - B_8) > non_crit_B && Math.abs(B - B_9) > non_crit_B
                        && Math.abs(B - B_10) > non_crit_B)
*/
                {
                    pw.printf("%f %E%n", B, I_ud_B[i]);
                    //System.out.println(B + " " + M_B[i]);
                    //System.out.println(B + " " + F_B[i]);
                }

            }

            pw.close();
        } catch (IOException ee) {
            System.err.println("ошибка открытия потока " + ee);
        } finally {
            pw.close();
        }
    }

}

