import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;
import java.util.Calendar;


public class LAPR1TurmaDEFGrupo03 {
    //CAMINHO JOANA C:\\Users\\joana\\OneDrive\\Ambiente de Trabalho\\exemploRegistoNumerosCovid19.csv
    //CAMINHO MIGUEL C:\Users\Miguel\Documents\exemploRegistoNumerosCovid19.csv
    //CAMINHO BRUNA C:\Users\Bruna\Downloads\exemploRegistoNumerosCovid19.csv

    public static void main(String[] args) throws IOException, ParseException {
        int MOD = verificacaoModo(args);
        switch (MOD) {
            case 0:
                System.out.println("Modo interativo");
                modoInterativo();
                break;
            case 1:
                System.out.println("Modo não interativo");
                modoNaoInterativo(args);
                break;
        }
    }

    public static int verificacaoModo(String[] args) {
        int count = args.length;
        if (count == 0) {
            return 0;
        } else
            return 1;
    }

    public static void modoNaoInterativo(String[] args) throws FileNotFoundException, ParseException {
       /* // java -jar nome programa.jar -r X -di DD-MM-AAAA -df DD-MM-AAAA -di1 DD-MMAAAA -df1 DD-MM-AAAA -di2 DD-MM-AAAA -df2 DD-MM-AAAA -T DD-MM-AAAA
        // registoNumeroTotalCovid19.csv registoNumerosAcumuladosCovid19.csv matrizTransicao.txt nome_ficheiro_saida.txt.
        String caminhoTotal, caminhoAcumulado, nomeFileOut1, nomeFileOut2;
        int res = Integer.parseInt(args[1]);
        caminhoTotal = args[16];
        caminhoAcumulado = args[17];
        nomeFileOut1 = args[18];
        nomeFileOut2 = args[19];
        String[][] matrixTotal = Scann(caminhoTotal);
        String[][] matrixAcumulado = Scann(caminhoAcumulado);

        //Matriz Diferença Periódica
        String di = args[3];
        int posDi = posicaoDatas(matrix, di);
        String df = args[5];
        int posDf = posicaoDatas(matrix, df);
        String[][] matrixDifPer = calculoDif(matrix, posDi, posDf, res);
        //Fim Matriz Diferença Periódica

        //Matriz Periodo1
        String di1 = args[7];
        int posDi1 = posicaoDatas(matrix, di1);
        String df1 = args[9];
        int posDf1 = posicaoDatas(matrix, df1);
        String[][] matrixDif1 = calculoPeriodo(matrix, posDi1, posDf1);
        //Fim Matriz Periodo1

        //Matriz Periodo2
        String di2 = args[11];
        int posDi2 = posicaoDatas(matrix, di2);
        String df2 = args[13];
        int posDf2 = posicaoDatas(matrix, df2);
        String[][] matrixDif2 = calculoPeriodo(matrix, posDi2, posDf2);
        //Fim Matriz Periodo2

        //Matriz PeriodoTotal
        String[][] matrixPeriodo = calculoDifPeriodo(matrixDif1, matrixDif2);
        //Fim Matriz PeriodoTotal

        //Matriz Previsão
        String T = args[15];
        int posT = posicaoDatas(matrix, T);
        String[][] matrixPrevisao;
        //Fim Matriz Previsão

        // ficheiro output*/
    }

    public static void modoInterativo() throws IOException, ParseException {
        Scanner sc = new Scanner(System.in);
        BufferedReader sc1;
        int uploadMOD = upload();
        String caminho = "";
        String[] cabecalho = new String[6];
        switch (uploadMOD) {
            case 0: {
                System.out.println("Indique o caminho para o ficheiro com os dados Acomulativos:");
                caminho = uploadAcomulativo();
                break;
            }
            case 1: {
                System.out.println("Indique o caminho para o ficheiro com os dados Totais:");
                caminho = uploadTotal();
                break;
            }
        }
        String[][] matrix = new String[99][6];
        matrix = Scann(caminho);
        sc1 = new BufferedReader(new FileReader(caminho));
        String line = sc1.readLine();
        cabecalho = line.split(",");

        int opcao = opcoesInterface();
        switch (opcao) {
            case 0: //Analizar dados de um dia
                System.out.println("Indique o dia a analizar: ");
                String dia = sc.nextLine();
                ValidarDataInicio(dia, 0);
                int posDia = posicaoDatas(matrix, dia);
                for (int i = 0; i < 6; i++) {
                    System.out.print(cabecalho[i] + " -> " + matrix[posDia][i]);
                    System.out.println();
                }
                break;
            case 1: //Analisar periodo de tempo
                int res = resolucaoInterface();
                System.out.println("Indique a data de início (AAAA-MM-DD): ");
                String di = recolhaDataInicio(res);
                int posDi = posicaoDatas(matrix, di);
                System.out.println("Indique a data final (AAAA-MM-DD): ");
                String df = recolhaDataFim(res);
                int posDf = posicaoDatas(matrix, df);
                calculoDif(matrix, posDi, posDf, res);
                break;
            case 2: //Analisar dados comparativamente a outro periodo de tempo (Acomulativo)
                if (uploadMOD == 1) {
                    System.out.println("Operação inválida: Ficheiro armazenado não possui dados suficientes!");
                    System.exit(0);
                } else {

                }
                break;
            case 3: //Analisar dados comparativamente a outro periodo de tempo (Total)
                if (uploadMOD == 0) {
                    System.out.println("Operação inválida: Ficheiro armazenado não possui dados suficientes!");
                    System.exit(0);
                } else {

                }
                break;
        }
        // ficheiro output
    }

    public static int upload() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Selecione o tipo de ficheito que pretende carregar:");
        System.out.println("Casos Covid19 Acomulativo -> 0");
        System.out.println("Casos Covid19 Total -> 1");
        int up = sc.nextInt();
        return up;
    }

    public static String uploadAcomulativo() {
        Scanner sc = new Scanner(System.in);
        String ficheiro = sc.nextLine();
        return ficheiro;
    }

    public static String uploadTotal() {
        Scanner sc = new Scanner(System.in);
        String ficheiro = sc.nextLine();
        return ficheiro;
    }

    public static int opcoesInterface() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Qual a análise que pretende realizar?");
        System.out.println("0 -> Analisar dados de um determinado dia.");
        System.out.println("1 -> Analisar dados de um periodo de tempo.");
        System.out.println("2 -> Analisar dados comparativamente a outro periodo de tempo (Acomulativo).");
        System.out.println("3 -> Analisar dados comparativamente a outro periodo de tempo (Total).");
        int opcao = sc.nextInt();
        return opcao;
    }

    public static int resolucaoInterface() {
        int resolucao;
        Scanner sc = new Scanner(System.in);
        System.out.println("Indique a resolução que pretende obter:");
        System.out.println("0 - resolução diária (será apresentada uma diferença diária dos parametros a analisar)");
        System.out.println("1 - resolução semanal (será apresentada uma diferença semanal dos parametros a analisar)");
        System.out.println("2 - resolução mensal (será apresentada uma diferença mensal dos parametros a analisar)");
        resolucao = sc.nextInt();
        if (resolucao != 0 && resolucao != 1 && resolucao != 2) {
            do {
                System.out.println("Introduza uma opção válida");
                resolucao = sc.nextInt();
            } while (resolucao != 0 && resolucao != 1 && resolucao != 2);
        }
        return resolucao;
    }

    public static String recolhaDataInicio(int res) throws ParseException {
        Scanner sc = new Scanner(System.in);
        String data = sc.nextLine();
        while (ValidarDataInicio(data, res) == 1) {
            System.out.println("Introduza uma data válida no formato AAAA-MM-DD");
            data = sc.nextLine();
        }
        return data;
    }

    public static String recolhaDataFim(int res) throws ParseException {
        Scanner sc = new Scanner(System.in);
        String data = sc.nextLine();
        while (ValidarDataFim(data, res) == 1) {
            System.out.println("Introduza uma data válida no formato AAAA-MM-DD");
            data = sc.nextLine();
        }
        return data;
    }

    public static int ValidarDataInicio(String input, int resolucao) throws ParseException {
        String formatString = "yyyy-MM-dd";
        SimpleDateFormat format = new SimpleDateFormat(formatString);
        try {
            format.setLenient(false);
            format.parse(input);
        } catch (ParseException e) {    //erro previsto (mês=13 ou formato errado, por exemplo)
            return 0;
        } catch (IllegalArgumentException e) {  //erro imprevisto (colocar letras em vez de números, por exemplo)
            return 0;
        }

        Calendar data = Calendar.getInstance();
        data.setTime(format.parse(input));
        switch (resolucao) {
            case 0:
                return 0;
            case 1:
                System.out.println("Indique uma data referente a uma segunda-feira");
                if (data.get(Calendar.DAY_OF_WEEK) == 2) {
                    return 0;
                }
            case 2:
                System.out.println("Indique uma data referente ao primeiro dia do mês");
                if (data.get(Calendar.DAY_OF_MONTH) == 1) {
                    return 0;
                }
        }

        return 1;
    }

    public static int ValidarDataFim(String input, int resolucao) throws ParseException {
        String formatString = "yyyy-MM-dd";
        SimpleDateFormat format = new SimpleDateFormat(formatString);
        try {
            format.setLenient(false);
            format.parse(input);
        } catch (ParseException e) {    //erro previsto (mês=13 ou formato errado, por exemplo)
            return 0;
        } catch (IllegalArgumentException e) {  //erro imprevisto (colocar letras em vez de números, por exemplo)
            return 0;
        }

        Calendar data = Calendar.getInstance();
        data.setTime(format.parse(input));
        switch (resolucao) {
            case 0:
                return 0;
            case 1:
                if (data.get(Calendar.DAY_OF_WEEK) == 1) {
                    return 0;
                }
            case 2:
                if (data.get(Calendar.DAY_OF_MONTH) == 1) {
                    return 0;
                }
        }

        return 1;
    }

    public static String[][] Scann(String caminho) throws FileNotFoundException {
        BufferedReader sc;
        String[][] ficheiro = new String[90][6];                                                                            // Verificar o número de linhas
        String line;
        int j = 0;

        try {
            sc = new BufferedReader(new FileReader(caminho));
            while ((line = sc.readLine()) != null) {
                ficheiro[j] = line.split(",");
     /*           for (int i = 0; i < 6; i++) {
                    System.out.print(ficheiro[j][i] + " ");
                }
                System.out.println(); */
                j++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ficheiro;
    }

    public static void guardarFicheiro() {
        Scanner sc = new Scanner(System.in);
        String caminho2 = sc.nextLine();
        try {
            try (FileWriter fw = new FileWriter(caminho2, true)) {
                String gravaTeste = "Output\r\n";
                fw.write(gravaTeste);
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    public static int posicaoDatas(String[][] ficheiro, String di) {
        int i;
        for (i = 0; i < 999; i++) {
            if (ficheiro[i][0].equals(di)) {
                return i;
            }
        }
        return i;
    }


    public static String[][] calculoDif(String[][] matrizDatas, int di, int df, int step) throws ParseException {
        String formatString = "yyyy-MM-dd";
        SimpleDateFormat format = new SimpleDateFormat(formatString);
        String[][] matrizDiferenca = new String[(df - di) + 1][6];
        int aux = 0;
        int j = di;
        int a = 0;
        String dt = matrizDatas[j][0];
        Calendar data = Calendar.getInstance();
        data.setTime(format.parse(dt));
        switch (step) {
            case 0:
                a = j + 1;
                break;
            case 1:
                data.add(Calendar.WEEK_OF_YEAR, +1);
                a = posicaoDatas(matrizDatas, data.toString());
                break;
            case 2:
                data.add(Calendar.MONTH, +1);
                a = posicaoDatas(matrizDatas, data.toString());
                break;
        }
        while (j <= df && a <= df) {
            matrizDiferenca[aux][0] = matrizDatas[a][0];
            for (int i = 1; i <= 6; i++) {
                matrizDiferenca[aux][i] = String.valueOf(Integer.parseInt(matrizDatas[a][i]) - Integer.parseInt(matrizDatas[j][i]));
            }
            dt = matrizDatas[a][0];
            data = Calendar.getInstance();
            data.setTime(format.parse(dt));
            j = a;
            switch (step) {
                case 0:
                    a++;
                    break;
                case 1:
                    data.add(Calendar.WEEK_OF_YEAR, +1);
                    a = posicaoDatas(matrizDatas, data.toString());
                    break;
                case 2:
                    data.add(Calendar.MONTH, +1);
                    a = posicaoDatas(matrizDatas, data.toString());
                    break;
            }
            aux++;
        }
        return matrizDiferenca;
    }

    public static String[][] calculoPeriodo(String[][] matrizDatas, int di, int df) {

        String[][] matrizPeriodo = new String[1][matrizDatas[0].length];
        matrizPeriodo[0][0] = matrizDatas[0][0];

        for (int i = 1; i <= matrizDatas[0].length; i++) {
            matrizPeriodo[0][i] = String.valueOf(Integer.parseInt(matrizDatas[df][i]) - Integer.parseInt(matrizDatas[di][i]));
        }

        return matrizPeriodo;
    }

    public static String[][] calculoDifPeriodo(String[][] matrizPer1, String[][] matrizPer2) {

        String[][] matrizPeriodoFinal = new String[1][matrizPer1[0].length];
        matrizPeriodoFinal[0][0] = "Diferença entre os Periodos";

        for (int i = 1; i <= matrizPer1[0].length; i++) {
            matrizPeriodoFinal[0][i] = String.valueOf(Integer.parseInt(matrizPer2[0][i]) - Integer.parseInt(matrizPer1[0][i]));
        }

        return matrizPeriodoFinal;
    }
}