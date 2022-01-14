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
        String acomulativo = "";
        String total = "";
        String[] cabecalho;
        switch (uploadMOD) {
            case 0 -> {
                System.out.println("Indique o caminho para o ficheiro com os dados Acomulativos:");
                acomulativo = uploadFicheiro();
            }
            case 1 -> {
                System.out.println("Indique o caminho para o ficheiro com os dados Totais:");
                total = uploadFicheiro();
            }
            case 2 -> {
                System.out.println("Indique o caminho para o ficheiro com os dados Acomulativos:");
                acomulativo = uploadFicheiro();
                System.out.println("Indique o caminho para o ficheiro com os dados Totais:");
                total = uploadFicheiro();
            }
        }

        String[][] acomulativoMatrix;
        acomulativoMatrix = Scann(acomulativo);
        String[][] totalMatrix;
        totalMatrix = Scann(total);
        String TXT = uploadTXT();
        //FUNÇAO SCANNER MATRIZ TRANSIÇÕES
        sc1 = new BufferedReader(new FileReader(caminho));
        String line = sc1.readLine();
        cabecalho = line.split(",");

        int opcao = opcoesInterface();
        int posDiUtil;

        switch (opcao) {
            case 0: //Analizar dados de um dia
                System.out.println("Indique o tipo de dados que pretende analisar:");
                System.out.println("Casos Covid19 Acomulativo -> 0");
                System.out.println("Novos casos Covid19 -> 1");
                int tipoDados = sc.nextInt();
                switch (tipoDados){
                    case 0:
                        casosDia(acomulativoMatrix, cabecalho);
                    case 1:
                        casosDia(totalMatrix, cabecalho);
                }

                break;
            case 1: //Analisar periodo de tempo
                int res = resolucaoInterface();
                System.out.println("Indique a data de início (AAAA-MM-DD): ");
                String di = recolhaData();
                int posDi = posicaoDatas(acomulativoMatrix, di);
                System.out.println("Indique a data final (AAAA-MM-DD): ");
                String df = recolhaData();
                int posDf = posicaoDatas(acomulativoMatrix, df);
                String[][] resultadosPeriodo;
                switch (res) {
                    case 0 -> {
                        resultadosPeriodo = calculoDif(acomulativoMatrix, posDi, posDf, res);
                        mostraDeResultados(resultadosPeriodo);
                    }
                    case 1 -> {
                        posDiUtil = posDataUtilSemana(posDi, acomulativoMatrix);
                        //System.out.println(posDiUtil);
                        resultadosPeriodo = calculoDif(acomulativoMatrix, posDiUtil, posDf, res);
                        mostraDeResultados(resultadosPeriodo);
                    }
                    case 2 -> {
                        posDiUtil = posDataUtilMes(posDi, acomulativoMatrix);
                        //System.out.println(posDiUtil);
                        resultadosPeriodo = calculoDif(acomulativoMatrix, posDiUtil, posDf, res);
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
        }
        // ficheiro output
    }

    public static int upload() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Selecione o tipo de ficheiro que pretende carregar:");
        System.out.println("Casos Covid19 Acomulativo -> 0");
        System.out.println("Casos Covid19 Total -> 1");
        System.out.println("Ambos os ficheiros -> 2");
        return sc.nextInt();
    }

    public static String uploadFicheiro() {
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }

    public static String uploadTXT() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Pretende carregar um ficheiro contendo uma matriz de transições?");
        System.out.println("Sim -> 0");
        System.out.println("Não -> 1");
        int sn = sc.nextInt();
        String TXT = "";
        switch (sn){
            case 0:
                System.out.println("Indique o caminho para o ficheiro TXT:");
                TXT = sc.nextLine();
                break;
            case 1:
                break;
        }
        return TXT;
    }


    public static int opcoesInterface() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Qual a análise que pretende realizar?");
        System.out.println("0 -> Analisar dados de um determinado dia.");
        System.out.println("1 -> Analisar dados de um periodo de tempo.");
        System.out.println("2 -> Analisar dados comparativamente a outro periodo de tempo (Acomulativo).");
        System.out.println("3 -> Analisar dados comparativamente a outro periodo de tempo (Total).");
        return sc.nextInt();
    }

    public static void casosDia(String[][] matrix, String[] cabecalho){
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

    public static String recolhaData(){
        Scanner sc = new Scanner(System.in);
        String data = sc.nextLine();
        while (ValidarData(data) == 0) {
            System.out.println("Introduza uma data válida no formato AAAA-MM-DD");
            data = sc.nextLine();
        }
        return data;
    }


    public static int ValidarData(String input){
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
        while(data.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY){
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
        while(data.get(Calendar.DAY_OF_MONTH) != 1){
            data.add(Calendar.DATE, 1);
            posDataUtil++;
        }
        return posDataUtil;
    }

    public static String[][] Scann(String caminho){
        BufferedReader sc;
        String[][] ficheiro = new String[90][6];                                                                            // Verificar o número de linhas
        String line;
        int j = 0;

        try {
            sc = new BufferedReader(new FileReader(caminho));
            while ((line = sc.readLine()) != null) {
                ficheiro[j] = line.split(",");
                j++;
            }
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
        System.out.println(ficheiro.length);
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

    public static void mostraDeResultados(String[][] matrix) throws ParseException{
        for (int i = 0; i <= matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.println(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }
}