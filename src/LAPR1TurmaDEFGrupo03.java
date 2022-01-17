import java.awt.desktop.ScreenSleepEvent;
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
            case 0 -> {
                System.out.println("Modo interativo");
                modoInterativo();
            }
            case 1 -> {
                System.out.println("Modo não interativo");
                modoNaoInterativo(args);
            }
        }
    }

    public static int verificacaoModo(String[] args) {
        int count = args.length;
        if (count == 0) {
            return 0;
        } else
            return 1;
    }

    public static void modoNaoInterativo(String[] args) throws ParseException {
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
        String acumulativo = "";
        String total = "";
        String[] cabecalho;
        String[][] acumulativoMatrix = new String[9999][6];
        String[][] totalMatrix = new String[9999][6];
        int linhasTotalMatrix = 0;
        int linhasAcumulativoMatrix = 0;
        switch (uploadMOD) {
            case 0 -> {
                System.out.println("Indique o caminho para o ficheiro com os dados Acumulativos:");
                caminho = acumulativo = uploadFicheiro();
                linhasAcumulativoMatrix = Scann(acumulativo, acumulativoMatrix);
            }
            case 1 -> {
                System.out.println("Indique o caminho para o ficheiro com os dados Totais:");
                caminho = total = uploadFicheiro();
                linhasTotalMatrix = Scann(total, totalMatrix);
            }
            case 2 -> {
                System.out.println("Indique o caminho para o ficheiro com os dados Acumulativos:");
                caminho = acumulativo = uploadFicheiro();
                linhasAcumulativoMatrix = Scann(acumulativo, acumulativoMatrix);
                System.out.println("Indique o caminho para o ficheiro com os dados Totais:");
                total = uploadFicheiro();
                linhasTotalMatrix = Scann(total, totalMatrix);
            }
        }
        acumulativoMatrix = new String[linhasAcumulativoMatrix][6];
        totalMatrix = new String[linhasTotalMatrix][6];
        double[][] matrizTransicao = uploadTXT();
        sc1 = new BufferedReader(new FileReader(caminho));
        String line = sc1.readLine();
        cabecalho = line.split(",");
        int opcao = opcoesInterface();
        int posDiUtil;
        switch (opcao) {
            case 0: //Analizar dados de um dia
                System.out.println("Indique o tipo de dados que pretende analisar:");
                System.out.println("0 -> Casos Covid19 Acumulativo");
                System.out.println("1 -> Novos Casos Covid19");
                int tipoDados = sc.nextInt();
                switch (tipoDados) {
                    case 0:
                        casosDia(acumulativoMatrix, cabecalho);
                    case 1:
                        casosDia(totalMatrix, cabecalho);
                }
                break;
            case 1: //Analisar periodo de tempo
                int res = resolucaoInterface();
                System.out.println("Indique a data de início (AAAA-MM-DD): ");
                String di = recolhaData();
                acumulativoMatrix = new String[linhasAcumulativoMatrix][6];
                int posDi = posicaoDatas(acumulativoMatrix, di);
                System.out.println("Indique a data final (AAAA-MM-DD): ");
                String df = recolhaData();
                int posDf = posicaoDatas(acumulativoMatrix, df);
                String[][] resultadosPeriodo;
                switch (res) {
                    case 0 -> {
                        resultadosPeriodo = calculoDif(acumulativoMatrix, posDi, posDf, res);
                        mostraDeResultados(resultadosPeriodo);
                    }
                    case 1 -> {
                        posDiUtil = posDataUtilSemana(posDi, acumulativoMatrix);
                        //System.out.println(posDiUtil);
                        resultadosPeriodo = calculoDif(acumulativoMatrix, posDiUtil, posDf, res);
                        mostraDeResultados(resultadosPeriodo);
                    }
                    case 2 -> {
                        posDiUtil = posDataUtilMes(posDi, acumulativoMatrix);
                        //System.out.println(posDiUtil);
                        resultadosPeriodo = calculoDif(acumulativoMatrix, posDiUtil, posDf, res);
                        mostraDeResultados(resultadosPeriodo);
                    }
                }
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
            case 4:
                if (uploadMOD == 0 || matrizTransicao == null) {
                    System.out.println("Operação inválida: Ficheiro armazenado não pode ser utilizado para fazer previsões!");
                    System.exit(0);
                } else {
                    System.out.println("Indique o dia para o qual pretende realizar a previsão.");
                    String data = sc.nextLine();
                    previsaoPandemia(totalMatrix, matrizTransicao, data, linhasTotalMatrix);
                }
                break;
            case 5:
                if (matrizTransicao == null) {
                    System.out.println("Operação inválida: Não foram introduzidos dados suficientes para obter o resultado desejado!");
                    System.exit(0);
                } else {
                    previsaoDiasAteMorrer(matrizTransicao);
                }
                break;
        }
        // ficheiro output
    }

    public static int upload() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Selecione o tipo de ficheiro que pretende carregar:");
        System.out.println("0 -> Casos Covid19 Acumulativo");
        System.out.println("1 -> Casos Covid19 Total");
        System.out.println("2 -> Ambos os ficheiros");
        return sc.nextInt();
    }

    public static String uploadFicheiro() {
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }

    public static double[][] uploadTXT() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Pretende carregar um ficheiro contendo uma matriz de transições?");
        System.out.println("0 -> Sim");
        System.out.println("1 -> Não");
        int sn = sc.nextInt();
        String TXT;
        double[][] matrizT = new double[5][5];
        switch (sn) {
            case 0:
                System.out.println("Indique o caminho para o ficheiro TXT:");
                TXT = sc.nextLine();
                matrizT = matrizTransicao(TXT);
                break;
            case 1:
                break;
        }
        return matrizT;
    }

    public static int opcoesInterface() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Qual a análise que pretende realizar?");
        System.out.println("0 -> Analisar dados de um determinado dia.");
        System.out.println("1 -> Analisar dados de um período de tempo.");
        System.out.println("2 -> Analisar dados comparativamente a outro período de tempo (Acumulativo).");
        System.out.println("3 -> Analisar dados comparativamente a outro período de tempo (Total).");
        System.out.println("4 -> Previsão de casos para um dia específico.");
        System.out.println("5 -> Previsão de dias até chegar a óbito.");
        return sc.nextInt();
    }

    public static void casosDia(String[][] matrix, String[] cabecalho) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Indique o dia a analizar: ");
        String dia = sc.nextLine();
        ValidarData(dia);
        int posDia = posicaoDatas(matrix, dia);
        for (int i = 0; i < 6; i++) {
            System.out.print(cabecalho[i] + " -> " + matrix[posDia][i]);
            System.out.println();
        }
    }

    public static int resolucaoInterface() {
        int resolucao;
        Scanner sc = new Scanner(System.in);
        System.out.println("Indique a resolução que pretende obter:");
        System.out.println("0 -> Resolução Diária (será apresentada uma diferença diária dos parâmetros a analisar)");
        System.out.println("1 -> Resolução Semanal (será apresentada uma diferença semanal dos parâmetros a analisar)");
        System.out.println("2 -> Resolução Mensal (será apresentada uma diferença mensal dos parâmetros a analisar)");
        resolucao = sc.nextInt();
        if (resolucao != 0 && resolucao != 1 && resolucao != 2) {
            do {
                System.out.println("Introduza uma opção válida!");
                resolucao = sc.nextInt();
            } while (resolucao != 0 && resolucao != 1 && resolucao != 2);
        }
        return resolucao;
    }

    public static String recolhaData() {
        Scanner sc = new Scanner(System.in);
        String data = sc.nextLine();
        while (ValidarData(data) == 0) {
            System.out.println("Introduza uma data válida (AAAA-MM-DD)");
            data = sc.nextLine();
        }
        return data;
    }

    public static int ValidarData(String input) {
        String formatString = "yyyy-MM-dd";
        SimpleDateFormat format = new SimpleDateFormat(formatString);
        try {
            format.setLenient(false);
            format.parse(input);
        } catch (ParseException | IllegalArgumentException e) {    //erro previsto (mês=13 ou formato errado, por exemplo)
            return 0;
        }
        return 1;
    }

    public static int posDataUtilSemana(int posDi, String[][] matriz) throws ParseException {
        String formatString = "yyyy-MM-dd";
        SimpleDateFormat format = new SimpleDateFormat(formatString);
        String input = matriz[posDi][0];
        Calendar data = Calendar.getInstance();
        data.setTime(format.parse(input));
        int posDataUtil = posDi;
        while (data.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            data.add(Calendar.DATE, 1);
            posDataUtil++;
        }
        return posDataUtil;
    }

    public static int posDataUtilMes(int posDi, String[][] matriz) throws ParseException {
        String formatString = "yyyy-MM-dd";
        SimpleDateFormat format = new SimpleDateFormat(formatString);
        String input = matriz[posDi][0];
        Calendar data = Calendar.getInstance();
        data.setTime(format.parse(input));
        int posDataUtil = posDi;
        while (data.get(Calendar.DAY_OF_MONTH) != 1) {
            data.add(Calendar.DATE, 1);
            posDataUtil++;
        }
        return posDataUtil;
    }

    public static int Scann(String caminho, String[][] ficheiro) throws FileNotFoundException {
        Scanner sc = new Scanner(new File(caminho));         // Verificar o número de linhas
        String line;
        int j = 0;
        while (sc.hasNextLine()) {
            line = sc.nextLine();
            for (int i = 0; i < 6; i++) {
                ficheiro[j] = line.split(",");
            }
            j++;
        }
        return j-1;
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
        for (i = 0; i < ficheiro.length; i++) {
            if (ficheiro[i][0].equals(di)) {
                System.out.println(i);
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
            case 0 -> a = j + 1;
            case 1 -> {
                data.add(Calendar.WEEK_OF_YEAR, +1);
                a = posicaoDatas(matrizDatas, data.toString());
            }
            case 2 -> {
                data.add(Calendar.MONTH, +1);
                a = posicaoDatas(matrizDatas, data.toString());
            }
        }
        while (j <= df && a <= df) {
            matrizDiferenca[aux][0] = matrizDatas[a][0];
            for (int i = 1; i <= 5; i++) {
                matrizDiferenca[aux][i] = String.valueOf(Integer.parseInt(matrizDatas[a][i]) - Integer.parseInt(matrizDatas[j][i]));
            }
            dt = matrizDatas[a][0];
            data = Calendar.getInstance();
            data.setTime(format.parse(dt));
            j = a;
            switch (step) {
                case 0 -> a++;
                case 1 -> {
                    data.add(Calendar.WEEK_OF_YEAR, +1);
                    a = posicaoDatas(matrizDatas, data.toString());
                }
                case 2 -> {
                    data.add(Calendar.MONTH, +1);
                    a = posicaoDatas(matrizDatas, data.toString());
                }
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

    public static void mostraDeResultados(String[][] matrix) {
        for (int i = 0; i <= matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.println(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static double[][] matrizTransicao(String TXT) {
        double[][] matrizT = new double[5][5];
        int linhas = 0;
        String line;
        String avancarLinha;
        try {
            Scanner sc = new Scanner(new FileReader(TXT));
            while ((sc.hasNextLine())) {
                for (int i = 0; i < 5; i++) {
                    line = sc.nextLine();
                    line = line.trim();
                    line = line.substring(4);
                    matrizT[linhas][i] = Double.parseDouble(line);
                }
                linhas++;
                if (sc.hasNextLine()) {
                    avancarLinha = sc.nextLine();
                }
            }
            sc.close();
        } catch (FileNotFoundException | NumberFormatException e) {
            e.printStackTrace();
        }
        return matrizT;
    }

    public static double[][] previsaoPandemia(String[][] matriz, double[][] matrizT, String date, int max) throws ParseException {
        String formatString = "yyyy-MM-dd";
        int i = 0, k;
        double[][] previsao = new double[1][5];
        double[][] matrizP = new double[5][5];
        SimpleDateFormat format = new SimpleDateFormat(formatString);
        Calendar data = Calendar.getInstance();
        data.setTime((format.parse(matriz[0][0])));
        while (!data.equals(date) && i <= matriz.length) {
            i++;
            if (i <= matriz.length) {
                data.setTime((format.parse(matriz[i][0])));
            }
        }
        if (i == max + 1) {                                                                                                       // ver max matriz.
            i = max;
            k = 0;
            data.setTime((format.parse(matriz[i][0])));
            while (!data.equals(date)) {
                k++;
                data.add(Calendar.DATE, 1);
            }
        } else {
            i--;
            k = 1;
        }
        for (int j = 0; j < 5; j++) {
            matrizP[j][j] = 1;
        }
        for (int p = 1; p <= k; p++) {
            for (int j = 0; j < matrizT.length; j++) {
                for (int l = 0; l < matrizT[0].length; l++) {
                    for (int m = 0; m < matrizT[0].length; m++) {
                        matrizP[j][l] = matrizP[j][m] * matrizT[m][l];
                    }
                }
            }
        }
        for (int j = 1; j <= 5; j++) {
            for (int l = 1; l <= 5; l++) {
                previsao[0][j] = Double.parseDouble(matriz[i][l]) * matrizP[l][j];
            }
        }
        return previsao;
    }

    public static double[][] subtracaoMatrizTransicao(double[][] matrizQ, int n) {
        double I[][] = {{1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}};
        double[][] matrizIQ = new double[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrizIQ[i][j] = I[i][j] - matrizQ[i][j];
            }
        }
        return matrizIQ;
    }

    public static double[][] decomposicaoLU(double[][] matrizA, int n) {
        double[][] matrizL = new double[n][n];
        double[][] matrizU = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int k = i; k < n; k++) {
                int soma = 0;
                for (int j = 0; j < i; j++) {
                    soma += (matrizL[i][j] * matrizU[j][k]);
                }
                matrizU[i][k] = matrizA[i][k] - soma;
            }
            for (int k = i; k < n; k++) {
                if (i == k) {
                    matrizL[i][i] = 1;
                } else {
                    int soma = 0;
                    for (int j = 0; j < i; j++) {
                        soma += (matrizL[k][j] * matrizU[j][i]);
                    }
                    matrizL[k][i] = (matrizA[k][i] - soma) / matrizU[i][i];
                }
            }
        }

        System.out.println("Triangular Inferior");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.println(matrizL[i][j]);
            }
            System.out.println();
        }

        System.out.println("Triangular Superior");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.println(matrizU[i][j] + " ");
            }
            System.out.println();
        }
        return matrizA;                                                                                                 //RETURN????
    }

    public static double[][] previsaoDiasAteMorrer(double[][] matrizTransicao) {
        double matriz1[][] = {{1, 1, 1, 1}};
        double[][] matrizInversaIQ = decomposicaoLU(subtracaoMatrizTransicao(matrizTransicao, 4), 4);
        double[][] diasAteMorrer = new double[1][4];
        for (int j = 0; j < 4; j++) {
            for (int l = 0; l < 4; l++) {
                diasAteMorrer[0][j] = matriz1[0][l] * matrizInversaIQ[l][j];
            }
        }
        return diasAteMorrer;
    }
}