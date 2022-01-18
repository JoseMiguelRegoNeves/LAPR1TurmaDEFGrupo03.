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
                String[][] acumulativoTemp = new String[linhasAcumulativoMatrix][6];
                System.arraycopy(acumulativoMatrix, 0, acumulativoTemp, 0, linhasAcumulativoMatrix);
                acumulativoMatrix = acumulativoTemp;
            }
            case 1 -> {
                System.out.println("Indique o caminho para o ficheiro com os dados Totais:");
                caminho = total = uploadFicheiro();
                linhasTotalMatrix = Scann(total, totalMatrix);
                String[][] totalTemp = new String[linhasTotalMatrix][6];
                System.arraycopy(totalMatrix, 0, totalTemp, 0, linhasTotalMatrix);
                totalMatrix = totalTemp;
            }
            case 2 -> {
                System.out.println("Indique o caminho para o ficheiro com os dados Acumulativos:");
                caminho = acumulativo = uploadFicheiro();
                linhasAcumulativoMatrix = Scann(acumulativo, acumulativoMatrix);
                String[][] acumulativoTemp = new String[linhasAcumulativoMatrix][6];
                System.arraycopy(acumulativoMatrix, 0, acumulativoTemp, 0, linhasAcumulativoMatrix);
                acumulativoMatrix = acumulativoTemp;
                System.out.println("Indique o caminho para o ficheiro com os dados Totais:");
                total = uploadFicheiro();
                linhasTotalMatrix = Scann(total, totalMatrix);
                String[][] totalTemp = new String[linhasTotalMatrix][6];
                System.arraycopy(totalMatrix, 0, totalTemp, 0, linhasTotalMatrix);
                totalMatrix = totalTemp;
            }
        }
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
                        break;
                    case 1:
                        casosDia(totalMatrix, cabecalho);
                        break;
                }
                break;
            case 1: //Analisar periodo de tempo
                int res = resolucaoInterface();
                System.out.println("Indique a data de início (AAAA-MM-DD): ");
                String di = recolhaData();
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
                        resultadosPeriodo = calculoDiferencaPeriodicaSemana(acumulativoMatrix, posDi, posDf);
                        mostraDeResultados(resultadosPeriodo);
                    }
                    case 2 -> {
                        //posDiUtil = posDataUtilMes(posDi, acumulativoMatrix);
                        resultadosPeriodo = calculoDiferencaPeriodicaMes(acumulativoMatrix, posDi, posDf);
                                //calculoDif(acumulativoMatrix, posDiUtil, posDf, res);
                        mostraDeResultados(resultadosPeriodo);
                    }
                }
                break;
            case 2: //Analisar dados comparativamente a outro periodo de tempo (Acomulativo)
                if (uploadMOD == 1) {
                    System.out.println("Operação inválida: Ficheiro armazenado não possui dados suficientes!");
                    System.exit(0);
                } else {
                    //calculoDifPeriodo(int posdi1, int posdf1, int posdi2, int posdf2, String[][] datas)
                }
                break;
            case 3: //Analisar dados comparativamente a outro periodo de tempo (Total)
                if (uploadMOD == 0) {
                    System.out.println("Operação inválida: Ficheiro armazenado não possui dados suficientes!");
                    System.exit(0);
                } else {
                    //calculoDifPeriodo(int posdi1, int posdf1, int posdi2, int posdf2, String[][] datas)
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
        System.out.println(data.get(Calendar.DAY_OF_MONTH));
        while (data.get(Calendar.DAY_OF_MONTH) != 1) {
            data.add(Calendar.DATE, 1);
            posDataUtil++;
        }
        System.out.println(posDataUtil);
        return posDataUtil;
    }



    public static int Scann(String caminho, String[][] ficheiro) throws FileNotFoundException {
        Scanner sc = new Scanner(new File(caminho));         // Verificar o número de linhas
        String line;
        int j = 0;
        sc.nextLine();
        while (sc.hasNextLine()) {
            line = sc.nextLine();
            String[] vetline = line.split(",");
            for (int i = 0; i < 6; i++) {
                ficheiro[j][i] = vetline[i];
                System.out.print(ficheiro[j][i] + " ");
            }
            System.out.println();
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
                return i;
            }
        }
        return i;
    }
    public static String[][] calculoDiferencaPeriodicaSemana(String[][] matrizDatas, int di, int df) throws ParseException {
        String formatString = "yyyy-MM-dd";
        SimpleDateFormat format = new SimpleDateFormat(formatString);
        Calendar data = Calendar.getInstance();
        int countDomingos = 0;
        int countSegundas = 0;
        int auxs=0;
        int auxd=0;
        int max=0;
        for (int i = di; i <= df; i++) {
            data.setTime(format.parse(matrizDatas[i][0]));
            if (data.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY){
                countSegundas++;

            }

            else if(data.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
                countDomingos++;
            }
        }
        int[] segundas = new int[countSegundas];
        int[] domingos = new int[countDomingos];
        for (int i = di; i <= df; i++) {
            data.setTime(format.parse(matrizDatas[i][0]));
            if (data.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY){
                segundas[auxs]=i;
                auxs++;
            }
            else if(data.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
                domingos[auxd]=i;
                auxd++;
            }
        }
        if(domingos[0]<segundas[0]){
            for (int j = 0; j < auxd-1; j++) {
                domingos[j]=domingos[j+1];
            }
            countDomingos--;
        }
        if(countDomingos>countSegundas){
            max=countSegundas;
        }
        else{
            max=countDomingos;
        }
        String[][] dif = new String[max][6];
        for (int i = 0; i < max; i++) {
            dif[i][0] = matrizDatas[segundas[i]][0];
            for (int j = 1; j < 6; j++) {
                dif[i][j] = String.valueOf(Integer.parseInt(matrizDatas[domingos[i]][j]) - Integer.parseInt(matrizDatas[segundas[i]][j]));
            }
        }
        return dif;
    }

    public static String[][] calculoDiferencaPeriodicaMes(String[][] matrizDatas, int di, int df) throws ParseException {
        String formatString = "yyyy-MM-dd";
        SimpleDateFormat format = new SimpleDateFormat(formatString);
        Calendar data = Calendar.getInstance();
        int countPrimDias = 0;
        int countUltDias = 0;
        int auxpd=0;
        int auxud=0;
        int max=0;
        for (int i = di; i <= df; i++) {
            data.setTime(format.parse(matrizDatas[i][0]));
            if (data.get(Calendar.DAY_OF_MONTH) == data.getMinimum(Calendar.DAY_OF_MONTH)){
                countPrimDias++;
            }

            else if(data.get(Calendar.DAY_OF_MONTH) == data.getMaximum(Calendar.DAY_OF_MONTH)){
                countUltDias++;
            }
        }
        int[] primDias = new int[countPrimDias];
        int[] ultDias = new int[countUltDias];
        System.out.println(data.getMaximum(Calendar.DAY_OF_MONTH));
        for (int i = di; i <= df; i++) {
            data.setTime(format.parse(matrizDatas[i][0]));
            if (data.get(Calendar.DAY_OF_MONTH) == data.getMinimum(Calendar.DAY_OF_MONTH)){
                primDias[auxpd]=i;
                auxpd++;
                System.out.println("minimo");
                System.out.println(i);
            }
            else if(data.get(Calendar.DAY_OF_MONTH) == data.getMaximum(Calendar.DAY_OF_MONTH)){
                ultDias[auxud]=i;
                auxud++;
                System.out.println("maximo");
                System.out.println(i);
            }
        }
        if(ultDias[0]<primDias[0]){
            for (int j = 0; j < auxud-1; j++) {
                ultDias[j]=primDias[j+1];
            }
            countUltDias--;
        }
        if(countUltDias>countPrimDias){
            max=countPrimDias;
        }
        else{
            max=countUltDias;
        }
        String[][] dif = new String[max][6];
        for (int i = 0; i < max; i++) {
            dif[i][0] = matrizDatas[primDias[i]][0];
            for (int j = 1; j < 6; j++) {
                dif[i][j] = String.valueOf(Integer.parseInt(matrizDatas[ultDias[i]][j]) - Integer.parseInt(matrizDatas[primDias[i]][j]));
            }
        }
        return dif;
    }

    public static String[][] calculoDifPeriodo(int posdi1, int posdf1, int posdi2, int posdf2, String[][] datas) {
        int dimPer1 = (posdi1 - posdf1) + 1;
        int dimPer2 = (posdi2 - posdf2) + 1;
        int dimComp = 0;
        int j = 0;

        if (dimPer1 > dimPer2){
            dimComp = dimPer2;
        }
        else{
            dimComp = dimPer1;
        }

        String[][] difPer = new String[dimComp][17];

        for (int i = posdi1; i < posdf1; i++) {
            for (int k = 0; k < 6; k++) {
                difPer[j][k]=datas[i][k];
            }
            j++;
        }

        for (int i = posdi2; i < posdf2; i++) {
            for (int k = 0; k < 6; k++) {
                difPer[j][k+6]=datas[i][k];
            }
            j++;
        }

        for (int i = 0; i < dimComp; i++) {
            difPer[i][12] = String.valueOf(Integer.parseInt(difPer[i][7]) - Integer.parseInt(difPer[i][1]));
        }

        for (int i = 0; i < dimComp; i++) {
            difPer[i][13] = String.valueOf(Integer.parseInt(difPer[i][8]) - Integer.parseInt(difPer[i][2]));
        }

        for (int i = 0; i < dimComp; i++) {
            difPer[i][14] = String.valueOf(Integer.parseInt(difPer[i][9]) - Integer.parseInt(difPer[i][3]));
        }

        for (int i = 0; i < dimComp; i++) {
            difPer[i][15] = String.valueOf(Integer.parseInt(difPer[i][10]) - Integer.parseInt(difPer[i][4]));
        }

        for (int i = 0; i < dimComp; i++) {
            difPer[i][16] = String.valueOf(Integer.parseInt(difPer[i][11]) - Integer.parseInt(difPer[i][5]));
        }
        return difPer;
    }

    public static void mostraDeResultados(String[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
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
        if (i == max + 1) {
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

        for (int i = 0; i < 4; i++) {
            for (int k = i; k < 4; k++) {
                int soma = 0;
                for (int j = 0; j < i; j++) {
                    soma += (matrizL[i][j] * matrizU[j][k]);
                }
                matrizU[i][k] = matrizA[i][k] - soma;
            }
            for (int k = i; k < 4; k++) {
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

        System.out.println("L");
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.print(matrizL[i][j] + " ");
            }
            System.out.println();
        }

        System.out.println("U");
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                System.out.print(matrizU[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("L inversa");

        double[][] matrizLinversa = new double[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (i==j) matrizLinversa[i][j] = 1;
                if (i<j) matrizLinversa[i][j] = 0;
                if (i>j) {
                    matrizLinversa[1][0] = - (matrizL[1][0] / matrizL[1][1]);
                    matrizLinversa[2][0] = - (matrizL[2][0] + (matrizLinversa[1][0] * matrizL[2][1]));
                    matrizLinversa[3][0] = - (matrizL[3][0] + (matrizL[3][1] * matrizLinversa[1][0]) + (matrizL[3][2] * matrizLinversa[2][0]));
                    matrizLinversa[2][1] = - matrizL[2][1] / matrizL[2][2];
                    matrizLinversa[3][1] = - matrizL[3][1] - (matrizL[3][2] * matrizLinversa[2][1]);
                    matrizLinversa[3][2] = - matrizL[3][2];
                }
                System.out.print(matrizLinversa[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("U inversa");

        double[][] matrizUinversa = new double[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (i>j) matrizUinversa[i][j] = 0;
                if (i<=j) {
                    matrizUinversa[0][0] = 1 / matrizU[0][0];
                    matrizUinversa[1][1] = 1 / matrizU[1][1];
                    matrizUinversa[0][1] = -((matrizU[0][1] * matrizUinversa[1][1]))/ matrizU[0][0];
                    matrizUinversa[2][2] = 1 / matrizU[2][2];
                    matrizUinversa[1][2] = - (matrizU[1][2] * matrizUinversa[2][2]) / matrizU[1][1];
                    matrizUinversa[0][2] = (- ((matrizU[0][1] * matrizUinversa[1][2]) + (matrizU[0][2]*matrizUinversa[2][2])))/matrizU[0][0];
                    matrizUinversa[3][3] = 1 / matrizU[3][3];
                    matrizUinversa[2][3] = (- ((matrizU[2][3] * matrizUinversa[3][3])))/matrizU[2][2];
                    matrizUinversa[1][3] = - ((matrizU[1][2] * matrizUinversa[2][3]) + (matrizU[1][3] * matrizUinversa[3][3]))/matrizU[1][1];
                    matrizUinversa[0][3] = - ((matrizU[0][1] * matrizUinversa[1][3]) + (matrizU[0][2] * matrizUinversa[2][3]) + (matrizU[0][3] * matrizUinversa[3][3]))/matrizU[0][0];
                }
                System.out.print(matrizUinversa[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("I-Q inversa");
        double[][] inversaIQ = new double[4][4];
        for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){
                inversaIQ[i][j]=0;
                for(int k=0;k<4;k++)
                {
                    inversaIQ[i][j]+=matrizLinversa[i][k]*matrizUinversa[k][j];
                }//end of k loop
                System.out.print(inversaIQ[i][j]+" ");  //printing matrix element
            }//end of j loop
            System.out.println();//new line
        }
        return inversaIQ;                                                                                                 //RETURN????
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