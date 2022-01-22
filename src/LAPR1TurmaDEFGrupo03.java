import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.lang.Math;
import java.util.concurrent.TimeUnit;


public class LAPR1TurmaDEFGrupo03 {

    //CAMINHOS MIGUEL ACOMULATIVO C:\Users\Miguel\IdeaProjects\LAPR1_TurmaDEF_Grupo03\src\exemploRegistoNumerosCovid19.csv
    //CAMINHOS MIGUEL TOTAL C:\Users\Miguel\IdeaProjects\LAPR1_TurmaDEF_Grupo03\src\totalPorEstadoCovid19EmCadaDia.csv
    //CAMINHOS MIGUEL TRANSIÇÃO C:\Users\Miguel\IdeaProjects\LAPR1_TurmaDEF_Grupo03\src\MatrizTransicao.txt

    public static void main(String[] args) throws IOException, ParseException {

        int MOD = verificacaoModo(args);
        switch (MOD) {
            case 0 -> {
                System.out.println("―――――――――― M O D O  I N T E R A T I V O \uD83D\uDC4B ――――――――――");
                modoInterativo();
            }
            case 1 -> {
                System.out.println("―――――――――― M O D O  N Ã O  I N T E R A T I V O \uD83D\uDDA5 ――――――――――");
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

    public static void modoNaoInterativo(String[] args) throws ParseException, IOException {
        int res = -1, linhasTotalMatrix, linhasAcumulativoMatrix, posDi, posDi1, posDi2, posDf, posDf1, posDf2;
        int difComp;
        String di = null, df = null, di1 = null, df1 = null, di2 = null, df2 = null, dia = null, output = null;
        String[] previsao, previsaoDiaMorte;
        String[][] acumulativoMatrix = new String[9999][6];
        String[][] totalMatrix = new String[9999][6];
        String[][] totalTemp, acumulativoTemp, difPer;
        String[] data = new String[1];
        String[][] desvioPadrao;
        String[][] media;
        String[][] cabecalhoCompPer = new String[1][17];
        cabecalhoCompPer[0][0] = "DataPer1"; cabecalhoCompPer[0][1] = "NãoInfetados"; cabecalhoCompPer[0][2] = "Infetados"; cabecalhoCompPer[0][3] = "Internados"; cabecalhoCompPer[0][4] = "UCI"; cabecalhoCompPer[0][5] = "Óbitos";
        cabecalhoCompPer[0][6] = "DataPer2"; cabecalhoCompPer[0][7] = "NãoInfetados"; cabecalhoCompPer[0][8] = "Infetados"; cabecalhoCompPer[0][9] = "Internados"; cabecalhoCompPer[0][10] = "UCI"; cabecalhoCompPer[0][11] = "Óbitos";
        cabecalhoCompPer[0][12] = "NãoInfetados"; cabecalhoCompPer[0][13] = "Infetados"; cabecalhoCompPer[0][14] = "Internados"; cabecalhoCompPer[0][15] = "UCI"; cabecalhoCompPer[0][16] = "Óbitos";


        double[][] matrizT;
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-r" -> res = Integer.parseInt(args[i + 1]);
                case "-di" -> di = args[i + 1];
                case "-df" -> df = args[i + 1];
                case "-di1" -> di1 = args[i + 1];
                case "-df1" -> df1 = args[i + 1];
                case "-di2" -> di2 = args[i + 1];
                case "-df2" -> df2 = args[i + 1];
                case "-T" -> dia = args[i + 1];
            }
            if (i == (args.length - 1)) output = args[i];
        }
        switch (args.length) {
            case 5 -> {
                //Recolha Matrizes
                //Rgisto Numeros Total Covid19.
                linhasTotalMatrix = Scann(args[2], totalMatrix);
                totalTemp = new String[linhasTotalMatrix][6];
                System.arraycopy(totalMatrix, 0, totalTemp, 0, linhasTotalMatrix);
                totalMatrix = totalTemp;

                //Matriz Transição
                matrizT = matrizTransicao(args[3]);

                //Previsão evolução da pandemia
                previsao = previsaoPandemia(totalMatrix, matrizT, dia);
                data[0] = dia;
                guardarFicheiro(output, data);
                guardarFicheiro(output, previsao);


                //Previsão dias até morrer
                previsaoDiaMorte = previsaoDiasAteMorrer(matrizT);
                guardarFicheiro(output, previsaoDiaMorte);
            }
            case 16 -> {
                //Recolha Matrizes
                //Registo Numeros Acumulados Covid19.
                linhasAcumulativoMatrix = Scann(args[14], acumulativoMatrix);
                acumulativoTemp = new String[linhasAcumulativoMatrix][6];
                System.arraycopy(acumulativoMatrix, 0, acumulativoTemp, 0, linhasAcumulativoMatrix);
                acumulativoMatrix = acumulativoTemp;

                posDi = posicaoDatas(acumulativoMatrix, di);
                posDf = posicaoDatas(acumulativoMatrix, df);
                String[][] resultadosPeriodo = new String[posDf - posDi][6];
                switch (res) {
                    case 0 -> {
                        resultadosPeriodo = calculoDiferencaPeriodicaDiaria(acumulativoMatrix, posDi, posDf);
                        mostraDeResultados(resultadosPeriodo);
                    }
                    case 1 -> {
                        resultadosPeriodo = calculoDiferencaPeriodicaSemana(acumulativoMatrix, posDi, posDf);
                        mostraDeResultados(resultadosPeriodo);
                    }
                    case 2 -> {
                        resultadosPeriodo = calculoDiferencaPeriodicaMes(acumulativoMatrix, posDi, posDf);
                        mostraDeResultados(resultadosPeriodo);
                    }
                }


                for (int i = 0; i < resultadosPeriodo.length; i++) {
                    guardarFicheiro(output, resultadosPeriodo[i]);
                }

                posDi1 = posicaoDatas(acumulativoMatrix, di1);
                posDf1 = posicaoDatas(acumulativoMatrix, df1);
                posDi2 = posicaoDatas(acumulativoMatrix, di2);
                posDf2 = posicaoDatas(acumulativoMatrix, df2);
                difPer = calculoDifPeriodo(posDi1, posDf1, posDi2, posDf2, acumulativoMatrix);

                if (posDf1 - posDi1 > posDf2 - posDi2) {
                    difComp = posDf2 - posDi2;
                } else {
                    difComp = posDf1 - posDi2;
                }

                media = mediaPer(difPer);
                desvioPadrao = desvioPadraoPer(difPer, media);

                guardarFicheiro(output, cabecalhoCompPer[0]);

                for (int i = 0; i < difComp; i++) {
                    guardarFicheiro(output, difPer[i]);
                }

                String[] mediaCabecalho = new String[media.length];
                mediaCabecalho[0] = "MÉDIA ↓";
                guardarFicheiro(output, mediaCabecalho);
                for (int i = 0; i < media.length; i++) {
                    guardarFicheiro(output, media[i]);
                }
                String[] mediaDP = new String[desvioPadrao.length];
                mediaDP[0] = "DESVIO PADRÃO ↓";
                guardarFicheiro(output, mediaDP);
                for (int i = 0; i < desvioPadrao.length; i++) {
                    guardarFicheiro(output, desvioPadrao[i]);
                }
            }
            case 20 -> {
                //Rgisto Numeros Total Covid19.
                linhasTotalMatrix = Scann(args[16], totalMatrix);
                totalTemp = new String[linhasTotalMatrix][6];
                System.arraycopy(totalMatrix, 0, totalTemp, 0, linhasTotalMatrix);
                totalMatrix = totalTemp;

                //Registo Numeros Acumulados Covid19.
                linhasAcumulativoMatrix = Scann(args[17], acumulativoMatrix);
                acumulativoTemp = new String[linhasAcumulativoMatrix][6];
                System.arraycopy(acumulativoMatrix, 0, acumulativoTemp, 0, linhasAcumulativoMatrix);
                acumulativoMatrix = acumulativoTemp;

                //Matriz Transição
                matrizT = matrizTransicao(args[18]);

                //Calculo Diferença Periodo
                posDi = posicaoDatas(acumulativoMatrix, di);
                posDf = posicaoDatas(acumulativoMatrix, df);
                String[][] resultadosPeriodo = new String[posDf - posDi][6];
                switch (res) {
                    case 0 -> {
                        resultadosPeriodo = calculoDiferencaPeriodicaDiaria(acumulativoMatrix, posDi, posDf);
                        mostraDeResultados(resultadosPeriodo);
                    }
                    case 1 -> {
                        resultadosPeriodo = calculoDiferencaPeriodicaSemana(acumulativoMatrix, posDi, posDf);
                        mostraDeResultados(resultadosPeriodo);
                    }
                    case 2 -> {
                        resultadosPeriodo = calculoDiferencaPeriodicaMes(acumulativoMatrix, posDi, posDf);
                        mostraDeResultados(resultadosPeriodo);
                    }
                }

                for (int i = 0; i < resultadosPeriodo.length; i++) {
                    guardarFicheiro(output, resultadosPeriodo[i]);
                }

                //Calculo Diferença entre Periodos
                posDi1 = posicaoDatas(acumulativoMatrix, di1);
                posDf1 = posicaoDatas(acumulativoMatrix, df1);
                posDi2 = posicaoDatas(acumulativoMatrix, di2);
                posDf2 = posicaoDatas(acumulativoMatrix, df2);

                if (posDf1 - posDi1 > posDf2 - posDi2) {
                    difComp = posDf2 - posDi2;
                } else {
                    difComp = posDf1 - posDi2;
                }
                difPer = calculoDifPeriodo(posDi1, posDf1, posDi2, posDf2, acumulativoMatrix);
                media = mediaPer(difPer);
                desvioPadrao = desvioPadraoPer(difPer, media);

                guardarFicheiro(output, cabecalhoCompPer[0]);

                for (int i = 0; i < difComp; i++) {
                    guardarFicheiro(output, difPer[i]);
                }
                String[] mediaCabecalho = new String[media.length];
                mediaCabecalho[0] = "MÉDIA ↓";
                guardarFicheiro(output, mediaCabecalho);
                for (int i = 0; i < media.length; i++) {
                    guardarFicheiro(output, media[i]);
                }
                String[] mediaDP = new String[desvioPadrao.length];
                mediaDP[0] = "DESVIO PADRÃO ↓";
                guardarFicheiro(output, mediaDP);
                for (int i = 0; i < desvioPadrao.length; i++) {
                    guardarFicheiro(output, desvioPadrao[i]);
                }

                //Previsão evolução da pandemia
                previsao = previsaoPandemia(totalMatrix, matrizT, dia);
                data[0] = dia;
                guardarFicheiro(output, data);
                guardarFicheiro(output, previsao);

                //Previsão dias até morrer
                previsaoDiaMorte = previsaoDiasAteMorrer(matrizT);
                guardarFicheiro(output, previsaoDiaMorte);
            }
        }
    }

    public static void modoInterativo() throws IOException, ParseException {
        Scanner sc = new Scanner(System.in);
        BufferedReader sc1;
        int uploadMOD = upload();
        String caminho = "";
        String acumulativo;
        String total;
        String[] cabecalho;
        String[][] acumulativoMatrix = new String[9999][6];
        String[][] totalMatrix = new String[9999][6];
        int linhasTotalMatrix;
        int linhasAcumulativoMatrix;
        switch (uploadMOD) {
            case 0 -> {
                System.out.println("\uD83D\uDCC1 Indique o caminho para o ficheiro com os dados Acumulativos:");
                caminho = acumulativo = uploadFicheiro();
                linhasAcumulativoMatrix = Scann(acumulativo, acumulativoMatrix);
                String[][] acumulativoTemp = new String[linhasAcumulativoMatrix][6];
                System.arraycopy(acumulativoMatrix, 0, acumulativoTemp, 0, linhasAcumulativoMatrix);
                acumulativoMatrix = acumulativoTemp;
            }
            case 1 -> {
                System.out.println("\uD83D\uDCC1 Indique o caminho para o ficheiro com os dados Totais:");
                caminho = total = uploadFicheiro();
                linhasTotalMatrix = Scann(total, totalMatrix);
                String[][] totalTemp = new String[linhasTotalMatrix][6];
                System.arraycopy(totalMatrix, 0, totalTemp, 0, linhasTotalMatrix);
                totalMatrix = totalTemp;
            }
            case 2 -> {
                System.out.println("\uD83D\uDCC1 Indique o caminho para o ficheiro com os dados Acumulativos:");
                caminho = acumulativo = uploadFicheiro();
                linhasAcumulativoMatrix = Scann(acumulativo, acumulativoMatrix);
                String[][] acumulativoTemp = new String[linhasAcumulativoMatrix][6];
                System.arraycopy(acumulativoMatrix, 0, acumulativoTemp, 0, linhasAcumulativoMatrix);
                acumulativoMatrix = acumulativoTemp;
                System.out.println("\uD83D\uDCC1 Indique o caminho para o ficheiro com os dados Totais:");
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
        int endOrNot = 0;
        while (endOrNot == 0) {
            int opcao = opcoesInterface();
            sc.nextLine();
            switch (opcao) {
                case 0 -> { //Analizar dados de um dia
                    System.out.println("Indique o tipo de dados que pretende analisar:");
                    System.out.println("0 -> Casos Covid19 Acumulativo");
                    System.out.println("1 -> Novos Casos Covid19");
                    int tipoDados = sc.nextInt();
                    String[] casosD = new String[6];
                    switch (tipoDados) {
                        case 0: {
                            String formato = "yyyy-MM-dd";
                            casosD = casosDia(acumulativoMatrix, cabecalho, formato);
                        }
                        case 1: {
                            String formato = "dd-MM-yyy";
                            casosD = casosDia(totalMatrix, cabecalho, formato);
                        }
                    }
                    System.out.println();
                    System.out.println("Deseja guardar os dados em um ficheiro?");
                    System.out.println("0 -> SIM");
                    System.out.println("1 -> NÃO");
                    int resposta = sc.nextInt();
                    if (resposta != 0 && resposta != 1) {
                        System.out.println("OPERAÇÃO INVÁLIDA: Selecione outra opção.");
                    } else {
                        if (resposta == 0) {
                            String nomeTipoFicheiro = nomeTipoFicheiroGuardar();
                            guardarFicheiro(nomeTipoFicheiro, casosD);
                        }
                    }
                    System.out.println();
                    System.out.println("VOLTAR AO MENU -> 0");
                    System.out.println("FECHAR APLICAÇÃO -> 1");
                    endOrNot = sc.nextInt();
                    if (endOrNot == 1) System.out.println("Obrigada por utilizar a nossa aplicação!");
                }
                case 1 -> { //Analisar periodo de tempo
                    if (uploadMOD == 1) {
                        System.out.println("OPERAÇÃO INVÁLIDA: O ficheiro armazenado não é o indicado para a operação escolhida!");
                    } else {
                        String formato = "yyyy-MM-dd";
                        int res = resolucaoInterface();
                        System.out.println("Indique a data de início (AAAA-MM-DD): ");
                        String di = recolhaData(formato);
                        int posDi = posicaoDatas(acumulativoMatrix, di);
                        System.out.println("Indique a data final (AAAA-MM-DD): ");
                        String df = recolhaData(formato);
                        int posDf = posicaoDatas(acumulativoMatrix, df);
                        String[][] resultadosPeriodo = new String[posDf - posDi][6];
                        switch (res) {
                            case 0 -> {
                                resultadosPeriodo = calculoDiferencaPeriodicaDiaria(acumulativoMatrix, posDi, posDf);
                                mostraDeResultados(resultadosPeriodo);
                            }
                            case 1 -> {
                                resultadosPeriodo = calculoDiferencaPeriodicaSemana(acumulativoMatrix, posDi, posDf);
                                mostraDeResultados(resultadosPeriodo);
                            }
                            case 2 -> {
                                resultadosPeriodo = calculoDiferencaPeriodicaMes(acumulativoMatrix, posDi, posDf);
                                mostraDeResultados(resultadosPeriodo);
                            }
                        }
                        System.out.println();
                        System.out.println("Deseja guardar os dados em um ficheiro?");
                        System.out.println("0 -> SIM");
                        System.out.println("1 -> NÃO");
                        int resposta = sc.nextInt();
                        if (resposta != 0 && resposta != 1) {
                            System.out.println("OPERAÇÃO INVÁLIDA: Selecione outra opção.");
                        } else {
                            if (resposta == 0) {
                                String nomeTipoFicheiro = nomeTipoFicheiroGuardar();
                                for (int i = 0; i < resultadosPeriodo.length; i++) {
                                    guardarFicheiro(nomeTipoFicheiro, resultadosPeriodo[i]);
                                }
                            }
                        }
                    }
                    System.out.println();
                    System.out.println("0 -> VOLTAR AO MENU");
                    System.out.println("1 -> FECHAR APLICAÇÃO");
                    endOrNot = sc.nextInt();
                    if (endOrNot == 1) System.out.println("Obrigada por utilizar a nossa aplicação!");
                }
                case 2 -> { //Analisar dados comparativamente a outro periodo de tempo
                    String formato = "yyyy-MM-dd";
                    String[][] desvioPadrao = new String[1][17];
                    String[][] media = new String[1][17];
                    String[][] difPer = new String[9999][6];
                    String[][]cabecalhoCompPer = new String[1][17];
                    cabecalhoCompPer[0][0] = "DataPer1"; cabecalhoCompPer[0][1] = "Não Infetados"; cabecalhoCompPer[0][2] = "Infetados"; cabecalhoCompPer[0][3] = "Internados"; cabecalhoCompPer[0][4] = "UCI"; cabecalhoCompPer[0][5] = "Óbitos";
                    cabecalhoCompPer[0][6] = "DataPer2"; cabecalhoCompPer[0][7] = "Não Infetados"; cabecalhoCompPer[0][8] = "Infetados"; cabecalhoCompPer[0][9] = "Internados"; cabecalhoCompPer[0][10] = "UCI"; cabecalhoCompPer[0][11] = "Óbitos";
                    cabecalhoCompPer[0][12] = "Não Infetados"; cabecalhoCompPer[0][13] = "Infetados"; cabecalhoCompPer[0][14] = "Internados"; cabecalhoCompPer[0][15] = "UCI"; cabecalhoCompPer[0][16] = "Óbitos";
                    int difComp = 0;
                    if (uploadMOD == 1) {
                        System.out.println("OPERAÇÃO INVÁLIDA: O ficheiro armazenado não possui dados suficientes!");
                    } else {
                        System.out.println("Indique a data de início do 1º Periodo (AAAA-MM-DD): ");
                        String di1 = recolhaData(formato);
                        int posDi1 = posicaoDatas(acumulativoMatrix, di1);
                        System.out.println("Indique a data final do 1º Periodo (AAAA-MM-DD): ");
                        String df1 = recolhaData(formato);
                        int posDf1 = posicaoDatas(acumulativoMatrix, df1);
                        System.out.println("Indique a data de início do 2º Periodo (AAAA-MM-DD): ");
                        String di2 = recolhaData(formato);
                        int posDi2 = posicaoDatas(acumulativoMatrix, di2);
                        System.out.println("Indique a data final do 2º Periodo (AAAA-MM-DD): ");
                        String df2 = recolhaData(formato);
                        int posDf2 = posicaoDatas(acumulativoMatrix, df2);
                        if (posDf1 - posDi1 > posDf2 - posDi2) {
                            difComp = posDf2 - posDi2;
                        } else {
                            difComp = posDf1 - posDi1;
                        }
                        difPer = calculoDifPeriodo(posDi1, posDf1, posDi2, posDf2, acumulativoMatrix);
                        media = mediaPer(difPer);
                        desvioPadrao = desvioPadraoPer(difPer, media);
                        mostraDeResultados(cabecalhoCompPer);
                        mostraDeResultados(difPer);
                        System.out.println("MÉDIA ↓");
                        mostraDeResultados(media);
                        System.out.println("DESVIO PADRÃO ↓");
                        mostraDeResultados(desvioPadrao);
                    }
                    String[][] difTemp = new String[difComp][6];
                    System.arraycopy(difPer, 0, difTemp, 0, difComp);
                    difPer = difTemp;
                    System.out.println();
                    System.out.println("Deseja guardar os dados em um ficheiro?");
                    System.out.println("0 -> SIM");
                    System.out.println("1 -> NÃO");
                    int resposta = sc.nextInt();
                    if (resposta != 0 && resposta != 1) {
                        System.out.println("OPERAÇÃO INVÁLIDA: Selecione outra opção.");
                    } else {
                        if (resposta == 0) {
                            String nomeTipoFicheiro = nomeTipoFicheiroGuardar();
                            guardarFicheiro(nomeTipoFicheiro, cabecalhoCompPer[0]);
                            for (int i = 0; i < difComp; i++) {
                                guardarFicheiro(nomeTipoFicheiro, difPer[i]);
                            }
                            String[] mediaCabecalho = new String[media.length];
                            mediaCabecalho[0] = "MÉDIA ↓";
                            guardarFicheiro(nomeTipoFicheiro, mediaCabecalho);
                            for (int i = 0; i < media.length; i++) {
                                guardarFicheiro(nomeTipoFicheiro, media[i]);
                            }
                            String[] mediaDP = new String[desvioPadrao.length];
                            mediaDP[0] = "DESVIO PADRÃO ↓";
                            guardarFicheiro(nomeTipoFicheiro, mediaDP);
                            for (int i = 0; i < desvioPadrao.length; i++) {
                                guardarFicheiro(nomeTipoFicheiro, desvioPadrao[i]);
                            }
                        }
                    }
                    System.out.println();
                    System.out.println("0 -> VOLTAR AO MENU");
                    System.out.println("1 -> FECHAR APLICAÇÃO");
                    endOrNot = sc.nextInt();
                    if (endOrNot == 1) System.out.println("Obrigada por utilizar a nossa aplicação!");
                }
                case 3 -> {
                    String[] data = new String[1];
                    String[] previsao = new String[5];
                    if (uploadMOD == 0 || (matrizTransicao[0][0] == 0 && matrizTransicao[1][1] == 0 && matrizTransicao[2][2] == 0 && matrizTransicao[3][3] == 0 && matrizTransicao[4][4] == 0)) {
                        System.out.println("OPERAÇÃO INVÁLIDA: Ficheiro armazenado não pode ser utilizado para fazer previsões!");
                    } else {
                        System.out.println("Indique o dia para o qual pretende realizar a previsão (DD-MM-AAAA): ");
                        data[0] = sc.nextLine();
                        previsao = previsaoPandemia(totalMatrix, matrizTransicao, data[0]);
                        mostraPrevisaoPandemia(previsao, data[0]);
                    }
                    System.out.println();
                    System.out.println("Deseja guardar os dados em um ficheiro?");
                    System.out.println("0 -> SIM");
                    System.out.println("1 -> NÃO");
                    int resposta = sc.nextInt();
                    if (resposta != 0 && resposta != 1) {
                        System.out.println("OPERAÇÃO INVÁLIDA: Selecione outra opção.");
                    } else {
                        if (resposta == 0) {
                            String nomeTipoFicheiro = nomeTipoFicheiroGuardar();
                            guardarFicheiro(nomeTipoFicheiro, data);
                            guardarFicheiro(nomeTipoFicheiro, previsao);
                        }
                    }
                    System.out.println();
                    System.out.println("0 -> VOLTAR AO MENU");
                    System.out.println("1 -> FECHAR APLICAÇÃO");
                    endOrNot = sc.nextInt();
                    if (endOrNot == 1) System.out.println("Obrigada por utilizar a nossa aplicação!");
                }
                case 4 -> {
                    String[] previsao = new String[5];
                    if (matrizTransicao[0][0] == 0 && matrizTransicao[1][1] == 0 && matrizTransicao[2][2] == 0 && matrizTransicao[3][3] == 0 && matrizTransicao[4][4] == 0) {
                        System.out.println("OPERAÇÃO INVÁLIDA: Não foram introduzidos dados suficientes para obter o resultado desejado!");
                    } else {
                        previsao = previsaoDiasAteMorrer(matrizTransicao);
                        mostrarDiasAteMorrer(previsao);
                    }
                    System.out.println();
                    System.out.println("Deseja guardar os dados em um ficheiro?");
                    System.out.println("0 -> SIM");
                    System.out.println("1 -> NÃO");
                    int resposta = sc.nextInt();
                    if (resposta != 0 && resposta != 1) {
                        System.out.println("OPERAÇÃO INVÁLIDA: Selecione outra opção.");
                    } else {
                        if (resposta == 0) {
                            String nomeTipoFicheiro = nomeTipoFicheiroGuardar();
                            guardarFicheiro(nomeTipoFicheiro, previsao);
                        }
                    }
                    System.out.println();
                    System.out.println("0 -> VOLTAR AO MENU");
                    System.out.println("1 -> FECHAR APLICAÇÃO");
                    endOrNot = sc.nextInt();
                    if (endOrNot == 1) System.out.println("Obrigada por utilizar a nossa aplicação!");
                }
                case 5 -> testesUnitarios();
            }
        }
    }

    public static int upload() {
        Scanner sc = new Scanner(System.in);
        System.out.println("\uD83D\uDCC1 Selecione o tipo de ficheiro que pretende carregar:");
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
                sc.nextLine();
                System.out.println("\uD83D\uDCC1 Indique o caminho para o ficheiro TXT:");
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
        System.out.println("―――――――――――――――   M E N U   P R I N C I P A L   ―――――――――――――――");
        System.out.println();
        System.out.println("                  Qual o tipo de análise que pretende realizar?");
        System.out.println();
        System.out.println("0 -> Analisar dados de um determinado dia.");
        System.out.println("1 -> Analisar dados de um período de tempo.");
        System.out.println("2 -> Analisar dados de um período de tempo comparativamente a outro período de tempo.");
        System.out.println("3 -> Previsão de casos para um dia específico.");
        System.out.println("4 -> Previsão de dias até chegar a óbito.");
        System.out.println("5 -> Testar Funcionamento dos métodos desenvolvidos.");
        return sc.nextInt();
    }

    public static String[] casosDia(String[][] matrix, String[] cabecalho, String formato) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Indique o dia (" + formato + "):");
        String dia = sc.nextLine();
        ValidarData(dia, formato);
        int posDia = posicaoDatas(matrix, dia);
        for (int i = 0; i < 6; i++) {
            System.out.print(cabecalho[i] + " -> " + matrix[posDia][i]);
            System.out.println();
        }
        return matrix[posDia];
    }

    public static int resolucaoInterface() {
        int resolucao;
        Scanner sc = new Scanner(System.in);
        System.out.println("―――――――――――――――   M E N U   R E S O L U Ç Ã O   ―――――――――――――――");
        System.out.println();
        System.out.println("                    Indique a resolução que pretende obter:");
        System.out.println();
        System.out.println("0 -> Resolução Diária (será apresentada uma diferença diária dos parâmetros a analisar)");
        System.out.println("1 -> Resolução Semanal (será apresentada uma diferença semanal dos parâmetros a analisar)");
        System.out.println("2 -> Resolução Mensal (será apresentada uma diferença mensal dos parâmetros a analisar)");
        resolucao = sc.nextInt();
        if (resolucao != 0 && resolucao != 1 && resolucao != 2) {
            do {
                System.out.println("ERRO: INTRODUZA UMA OPÇÃO VÁLIDA!");
                resolucao = sc.nextInt();
            } while (resolucao != 0 && resolucao != 1 && resolucao != 2);
        }
        return resolucao;
    }

    public static String recolhaData(String formato) {
        Scanner sc = new Scanner(System.in);
        String data = sc.nextLine();
        while (ValidarData(data, formato) == 0) {
            System.out.println("ERRO: INTRODUZA UMA DATA VÁLIDA (AAAA-MM-DD)!");
            data = sc.nextLine();
        }
        return data;
    }

    public static int ValidarData(String input, String formatString) {
        SimpleDateFormat format = new SimpleDateFormat(formatString);
        try {
            format.setLenient(false);
            format.parse(input);
        } catch (ParseException | IllegalArgumentException e) {    //erro previsto (mês=13 ou formato errado, por exemplo)
            return 0;
        }
        return 1;
    }

    public static int Scann(String caminho, String[][] ficheiro) throws FileNotFoundException {
        Scanner sc = new Scanner(new File(caminho));
        String line;
        int j = 0;
        sc.nextLine();
        while (sc.hasNextLine()) {
            line = sc.nextLine();
            String[] vetline = line.split(",");
            System.arraycopy(vetline, 0, ficheiro[j], 0, 6);
            j++;
        }
        return j;
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

    public static String[][] calculoDiferencaPeriodicaDiaria(String[][] matrizDatas, int di, int df) throws ParseException {
        String formatString = "yyyy-MM-dd";
        SimpleDateFormat format = new SimpleDateFormat(formatString);
        String[][] matrizDiferenca = new String[(df - di)][6];
        int aux = 0;
        int j = di;
        int a = j + 1;
        String dt = matrizDatas[j][0];
        Calendar data = Calendar.getInstance();
        data.setTime(format.parse(dt));

        while (j <= df && a <= df) {
            matrizDiferenca[aux][0] = matrizDatas[a][0];
            for (int i = 1; i <= 5; i++) {
                matrizDiferenca[aux][i] = String.valueOf(Integer.parseInt(matrizDatas[a][i]) - Integer.parseInt(matrizDatas[j][i]));
            }
            dt = matrizDatas[a][0];
            data = Calendar.getInstance();
            data.setTime(format.parse(dt));
            j = a;
            a++;
            aux++;
        }
        return matrizDiferenca;
    }

    public static String[][] calculoDiferencaPeriodicaSemana(String[][] matrizDatas, int di, int df) throws ParseException {
        String formatString = "yyyy-MM-dd";
        SimpleDateFormat format = new SimpleDateFormat(formatString);
        Calendar data = Calendar.getInstance();
        int countDomingos = 0;
        int countSegundas = 0;
        int auxs = 0;
        int auxd = 0;
        int max;
        for (int i = di; i <= df; i++) {
            data.setTime(format.parse(matrizDatas[i][0]));
            if (data.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
                countSegundas++;

            } else if (data.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                countDomingos++;
            }
        }
        int[] segundas = new int[countSegundas];
        int[] domingos = new int[countDomingos];
        for (int i = di; i <= df; i++) {
            data.setTime(format.parse(matrizDatas[i][0]));
            if (data.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
                segundas[auxs] = i;
                auxs++;
            } else if (data.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                domingos[auxd] = i;
                auxd++;
            }
        }
        if (domingos[0] < segundas[0]) {
            if (auxd - 1 >= 0) System.arraycopy(domingos, 1, domingos, 0, auxd - 1);
            countDomingos--;
        }
        max = Math.min(countDomingos, countSegundas);
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
        int auxpd = 0;
        int auxud = 0;
        int max;
        for (int i = di; i <= df; i++) {
            data.setTime(format.parse(matrizDatas[i][0]));
            if (data.get(Calendar.DAY_OF_MONTH) == data.getMinimum(Calendar.DAY_OF_MONTH)) {
                countPrimDias++;
            } else if (data.get(Calendar.DAY_OF_MONTH) == data.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                countUltDias++;
            }
        }
        int[] primDias = new int[countPrimDias];
        int[] ultDias = new int[countUltDias];
        for (int i = di; i <= df; i++) {
            data.setTime(format.parse(matrizDatas[i][0]));
            if (data.get(Calendar.DAY_OF_MONTH) == data.getMinimum(Calendar.DAY_OF_MONTH)) {
                primDias[auxpd] = i;
                auxpd++;
            } else if (data.get(Calendar.DAY_OF_MONTH) == data.getActualMaximum(Calendar.DAY_OF_MONTH)) {
                ultDias[auxud] = i;
                auxud++;
            }
        }
        if (ultDias[0] < primDias[0]) {
            if (auxud - 1 >= 0) System.arraycopy(primDias, 1, ultDias, 0, auxud - 1);
            countUltDias--;
        }
        max = Math.min(countUltDias, countPrimDias);
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
        int dimPer1 = (posdf1 - posdi1) + 1;
        int dimPer2 = (posdf2 - posdi2) + 1;
        int dimComp;
        int j = 0;

        dimComp = Math.min(dimPer1, dimPer2);

        String[][] difPer = new String[dimComp][17];

        for (int i = posdi1; i <= posdf1; i++) {
            System.arraycopy(datas[i], 0, difPer[j], 0, 6);
            String.format(Locale.US, "%s", difPer[j]);
            j++;
            if (j > dimComp) break;
        }
        j = 0;
        for (int i = posdi2; i <= posdf2; i++) {
            System.arraycopy(datas[i], 0, difPer[j], 6, 6);
            String.format(Locale.US, "%s", difPer[j]);
            j++;
            if (j > dimComp) break;
        }

        for (int i = 0; i < dimComp; i++) {
            difPer[i][12] = String.format(Locale.US, "%s", Integer.parseInt(difPer[i][7]) - Integer.parseInt(difPer[i][1]));
        }

        for (int i = 0; i < dimComp; i++) {
            difPer[i][13] = String.format(Locale.US, "%s", Integer.parseInt(difPer[i][8]) - Integer.parseInt(difPer[i][2]));
        }

        for (int i = 0; i < dimComp; i++) {
            difPer[i][14] = String.format(Locale.US, "%s", Integer.parseInt(difPer[i][9]) - Integer.parseInt(difPer[i][3]));
        }

        for (int i = 0; i < dimComp; i++) {
            difPer[i][15] = String.format(Locale.US, "%s", Integer.parseInt(difPer[i][10]) - Integer.parseInt(difPer[i][4]));
        }

        for (int i = 0; i < dimComp; i++) {
            difPer[i][16] = String.format(Locale.US, "%s", Integer.parseInt(difPer[i][11]) - Integer.parseInt(difPer[i][5]));
        }
        return difPer;
    }

    public static String[][] mediaPer(String[][] difPer) {
        String[][] media = new String[1][17];
        media[0][0] = "DiasPeríodo1: ";
        media[0][6] = "DiasPeríodo2: ";
        for (int i = 1; i < 6; i++) {
            int soma = 0;
            for (int j = 0; j < difPer.length; j++) {
                soma = soma + Integer.parseInt(difPer[j][i]);
                media[0][i] = String.format(Locale.US, "%.4f", Double.valueOf(soma / difPer.length));
            }
        }
        for (int i = 7; i < difPer[0].length; i++) {
            int soma = 0;
            for (int j = 0; j < difPer.length; j++) {
                soma = soma + Integer.parseInt(difPer[j][i]);
                media[0][i] = String.format(Locale.US, "%.4f", Double.valueOf(soma / difPer.length));
            }
        }
        return media;
    }

    public static String[][] desvioPadraoPer(String[][] difPer, String[][] media) {
        String[][] desvioPadrao = new String[1][17];
        desvioPadrao[0][0] = "DiasPeríodo1: ";
        desvioPadrao[0][6] = "DiasPeríodo2: ";
        double fracao, x, xMenosMedia, dp;
        for (int i = 1; i < 6; i++) {
            double denominador = 0;
            for (int j = 0; j < difPer.length; j++) {
                xMenosMedia = Double.parseDouble(difPer[j][i]) - Double.parseDouble(media[0][i]);
                x = Math.pow(xMenosMedia, 2);
                denominador = denominador + x;
            }
            fracao = denominador / difPer.length;
            dp = Math.sqrt(fracao);
            desvioPadrao[0][i] = String.valueOf(dp);
        }
        for (int i = 7; i < 17; i++) {
            double denominador = 0;
            for (int j = 0; j < difPer.length; j++) {
                xMenosMedia = Double.parseDouble(difPer[j][i]) - Double.parseDouble(media[0][i]);
                x = Math.pow(xMenosMedia, 2);
                denominador = denominador + x;
            }
            fracao = denominador / difPer.length;
            dp = Math.sqrt(fracao);
            desvioPadrao[0][i] = String.format(Locale.US, "%.4f", dp);
        }
        return desvioPadrao;
    }

    public static void mostraDeResultados(String[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                System.out.printf(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    public static double[][] matrizTransicao(String TXT) {
        double[][] matrizT = new double[5][5];
        int linhas = 0;
        String line;
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
                    sc.nextLine();
                }
            }
            sc.close();
        } catch (FileNotFoundException | NumberFormatException e) {
            e.printStackTrace();
        }
        return matrizT;
    }

    public static int diasAteData(String dataInicial, String dataFinal) throws ParseException {
        String formatString = "dd-MM-yyyy";
        SimpleDateFormat format = new SimpleDateFormat(formatString);
        Date data1 = format.parse(dataInicial);
        Date data2 = format.parse(dataFinal);
        long differenceMil = Math.abs(data2.getTime() - data1.getTime());
        return (int) TimeUnit.DAYS.convert(differenceMil, TimeUnit.MILLISECONDS);
    }

    public static double[][] matrixXmatrix(double[][] matriz1, double[][] matriz2) {
        int matriz1L = matriz1.length, matrix1C = matriz1[0].length, matrix2L = matriz2.length, matrix2C = matriz2[0].length;
        if (matrix1C == matrix2L) {
            double[][] prodMatrix = new double[matriz1L][matrix2C];
            for (int i = 0; i < matriz1L; i++) {
                for (int j = 0; j < matrix2C; j++) {
                    double soma = 0;
                    for (int k = 0; k < matrix1C; k++) {
                        double prod = matriz1[i][k] * matriz2[k][j];
                        soma += prod;
                        prodMatrix[i][j] = soma;
                    }
                }
            }
            return prodMatrix;
        } else {
            System.out.println("ERRO!! Impossível Realizar Produto de Matrizes Quando o número de Colunas da Primeira não é igual ao número de Linahas da Segunda!!");
            return new double[1][1];
        }
    }

    public static double[][] matrixCopy(double[][] matriz) {
        final int LINHAS = matriz.length, COLUNAS = matriz[0].length;
        double[][] copia = new double[LINHAS][COLUNAS];
        for (int linha = 0; linha < LINHAS; linha++) {
            System.arraycopy(matriz[linha], 0, copia[linha], 0, COLUNAS);
        }
        return copia;
    }

    public static double[][] potencia(double[][] matriz, int expoente) {
        double[][] copiaMatriz = matrixCopy(matriz);
        double[][] pot = new double[5][5];
        double[][] backupMatrix = matrixCopy(matriz);
        for (int i = 1; i < expoente; i++) {
            pot = matrixCopy(matrixXmatrix(copiaMatriz, backupMatrix));
            copiaMatriz = matrixCopy(pot);
        }
        return pot;
    }

    public static String[] previsaoPandemia(String[][] matriz, double[][] matrizT, String date) throws ParseException {
        int existe = 0, pos = 0, k;
        double soma;
        String[] previsao = new String[5];
        double[][] matrizP = new double[5][5];
        double[][] matrizDados = new double[matriz.length][matriz[0].length - 1];

        for (int i = 0; i < matriz.length; i++) {
            for (int j = 1; j < matriz[0].length; j++) {
                matrizDados[i][j - 1] = Double.parseDouble(matriz[i][j]);
            }
        }

        for (int l = 0; l < matrizT.length; l++) {
            System.arraycopy(matrizT[l], 0, matrizP[l], 0, matrizT[0].length);
        }

        for (int i = 0; i < matriz.length; i++) {
            if (date.equals(matriz[i][0])) {
                existe = 1;
                pos = i;
            }
        }

        double[] matrizDia = new double[5];
        if (existe == 1) {
            System.arraycopy(matrizDados[pos - 1], 0, matrizDia, 0, 5);
            for (int i = 0; i < matrizP.length; i++) {
                soma = 0;
                for (int j = 0; j < matrizP[0].length; j++) {
                    soma = soma + matrizDia[j] * matrizP[i][j];
                }
                previsao[i] = String.format(Locale.US, "%.1f", soma);
            }
        } else {
            System.arraycopy(matrizDados[matriz.length - 1], 0, matrizDia, 0, 5);
            k = diasAteData(matriz[matriz.length - 1][0], date);
            double[][] pot = matrixCopy(potencia(matrizP, k));
            if (k==1) {
                for (int i = 0; i < matrizP.length; i++) {
                    soma = 0;
                    for (int j = 0; j < matrizP[0].length; j++) {
                        soma = soma + matrizDia[j] * matrizP[i][j];
                    }
                    previsao[i] = String.format(Locale.US, "%.1f", soma);
                }
            }
            else {
            for (int i = 0; i < pot.length; i++) {
                soma = 0;
                for (int j = 0; j < pot[0].length; j++) {
                    soma = soma + matrizDia[j] * pot[i][j];
                }
                previsao[i] = String.format(Locale.US, "%.1f", soma);
            }
            }
        }
        return previsao;
    }

    public static void mostraPrevisaoPandemia(String[] previsao, String data) {
        System.out.println("――――――――――   P R E V I S Ã O   D A   P A N D E M I A   ――――――――――");
        System.out.println();
        System.out.println("Data da Previsão -> " + data);
        System.out.println("Número de Não Infetados -> " + previsao[0]);
        System.out.println("Número de Infetados -> " + previsao[1]);
        System.out.println("Número de Hospitalizados -> " + previsao[2]);
        System.out.println("Número de Internados em Unidade de Cuidados Intensivos -> " + previsao[3]);
        System.out.println("Número de Óbitos -> " + previsao[4]);
    }

    public static double[][] subtracaoMatrizTransicao(double[][] matrizT) {
        double[][] matrizQ = new double[4][4];
        double[][] matrizIQ = new double[4][4];
        double[][] I = new double[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (i == j) {
                    I[i][j] = 1;
                } else {
                    I[i][j] = 0;
                }
            }
        }
        for (int i = 0; i < 4; i++) {
            System.arraycopy(matrizT[i], 0, matrizQ[i], 0, 4);

        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                matrizIQ[i][j] = I[i][j] - matrizQ[i][j];
            }
        }
        return matrizIQ;
    }

    public static double[][] decomposicaoLU(double[][] matrizIQ) {
        double[][] matrizL = new double[4][4];
        double[][] matrizU = new double[4][4];
        for (int i = 0; i < matrizIQ.length; i++) {
            matrizU[i][i] = 1;
        }
        for (int k = 0; k < matrizIQ.length; k++) {
            for (int i = k; i < matrizIQ.length; i++) {
                double aux = 0;
                for (int l = 0; l < k; l++) {
                    aux += matrizL[i][l] * matrizU[l][k];
                }
                matrizL[i][k] = matrizIQ[i][k] - aux;
            }
            for (int j = k + 1; j < matrizIQ.length; j++) {
                double aux = 0;
                for (int l = 0; l < k; l++) {
                    aux += matrizL[k][l] * matrizU[l][j];
                }
                matrizU[k][j] = (matrizIQ[k][j] - aux) / matrizL[k][k];
            }
        }

        double[][] matrizLinversa = new double[4][4];
        matrizLinversa[0][1] = matrizLinversa[0][2] = matrizLinversa[1][2] = matrizLinversa[0][3] = matrizLinversa[1][3] = matrizLinversa[2][3] = 0;
        for (int i = 0; i < 4; i++) {
            matrizLinversa[i][i] = 1 / matrizL[i][i];
        }
        matrizLinversa[1][0] = -(matrizL[1][0] * matrizLinversa[0][0]) / matrizL[1][1];
        matrizLinversa[2][0] = -(matrizL[2][0] * matrizLinversa[0][0] + matrizL[2][1] * matrizLinversa[1][0]) / matrizL[2][2];
        matrizLinversa[3][0] = -(matrizL[3][0] * matrizLinversa[0][0] + matrizL[3][1] * matrizLinversa[1][0] + matrizL[3][2] * matrizLinversa[2][0]) / matrizL[3][3];
        matrizLinversa[2][1] = -(matrizL[2][1] * matrizLinversa[1][1]) / matrizL[2][2];
        matrizLinversa[3][1] = -(matrizL[3][1] * matrizLinversa[1][1] + matrizL[3][2] * matrizLinversa[2][1]) / matrizL[3][3];
        matrizLinversa[3][2] = -(matrizL[3][2] * matrizLinversa[2][2]) / matrizL[3][3];


        double[][] matrizUinversa = new double[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (i > j) {
                    matrizUinversa[i][j] = 0;
                }
                if (i <= j) {
                    matrizUinversa[0][0] = 1 / matrizU[0][0];
                    matrizUinversa[1][1] = 1 / matrizU[1][1];
                    matrizUinversa[0][1] = -((matrizU[0][1] * matrizUinversa[1][1])) / matrizU[0][0];
                    matrizUinversa[2][2] = 1 / matrizU[2][2];
                    matrizUinversa[1][2] = -(matrizU[1][2] * matrizUinversa[2][2]) / matrizU[1][1];
                    matrizUinversa[0][2] = (-((matrizU[0][1] * matrizUinversa[1][2]) + (matrizU[0][2] * matrizUinversa[2][2]))) / matrizU[0][0];
                    matrizUinversa[3][3] = 1 / matrizU[3][3];
                    matrizUinversa[2][3] = (-((matrizU[2][3] * matrizUinversa[3][3]))) / matrizU[2][2];
                    matrizUinversa[1][3] = -((matrizU[1][2] * matrizUinversa[2][3]) + (matrizU[1][3] * matrizUinversa[3][3])) / matrizU[1][1];
                    matrizUinversa[0][3] = -((matrizU[0][1] * matrizUinversa[1][3]) + (matrizU[0][2] * matrizUinversa[2][3]) + (matrizU[0][3] * matrizUinversa[3][3])) / matrizU[0][0];
                }
            }
        }
        double[][] inversaIQ = new double[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                inversaIQ[i][j] = 0;
                for (int k = 0; k < 4; k++) {
                    inversaIQ[i][j] += matrizUinversa[i][k] * matrizLinversa[k][j];
                }
            }

        }
        return inversaIQ;
    }

    public static String[] previsaoDiasAteMorrer(double[][] matrizT) {

        double[][] matrizInversaIQ = decomposicaoLU(subtracaoMatrizTransicao(matrizT));
        String[] diasAteMorrer = new String[4];

        diasAteMorrer[0] = String.format(Locale.US, "%.1f", matrizInversaIQ[0][0] + matrizInversaIQ[1][0] + matrizInversaIQ[2][0] + matrizInversaIQ[3][0]);
        diasAteMorrer[1] = String.format(Locale.US, "%.1f", matrizInversaIQ[0][1] + matrizInversaIQ[1][1] + matrizInversaIQ[2][1] + matrizInversaIQ[3][1]);
        diasAteMorrer[2] = String.format(Locale.US, "%.1f", matrizInversaIQ[0][2] + matrizInversaIQ[1][2] + matrizInversaIQ[2][2] + matrizInversaIQ[3][2]);
        diasAteMorrer[3] = String.format(Locale.US, "%.1f", matrizInversaIQ[0][3] + matrizInversaIQ[1][3] + matrizInversaIQ[2][3] + matrizInversaIQ[3][3]);
        return diasAteMorrer;
    }

    public static void mostrarDiasAteMorrer(String[] diasAteMorrer) throws FileNotFoundException {
        Scanner sc = new Scanner(System.in);
        System.out.println("――――――――――――   A T É   C H E G A R   A   Ó B I T O   ――――――――――――");
        System.out.println();
        System.out.println("Dias de um Não Infetado -> " + diasAteMorrer[0]);
        System.out.println("Dias de um Infetado -> " + diasAteMorrer[1]);
        System.out.println("Dias de um Hospitalizado -> " + diasAteMorrer[2]);
        System.out.println("Dias de um Internado em Unidade de Cuidados Intensivos -> " + diasAteMorrer[3]);
    }

    public static String nomeTipoFicheiroGuardar() {
        Scanner sc = new Scanner(System.in);
        System.out.println("\uD83D\uDCC1 Indique o nome do Ficheiro: ");
        String nomeficheiro = sc.nextLine();
        while (nomeficheiro.contains("|") || nomeficheiro.contains("\\") || nomeficheiro.contains("?") || nomeficheiro.contains("*") || nomeficheiro.contains("<") || nomeficheiro.contains("'") || nomeficheiro.contains(";") || nomeficheiro.contains(":") || nomeficheiro.contains(">") || nomeficheiro.contains("/") || nomeficheiro.contains(".txt") || nomeficheiro.contains(".csv")) {
            System.out.println("ERRO: O nome do ficheiro não é válido!");
            System.out.println();
            System.out.println("\uD83D\uDCC1 Insira o nome do ficheiro que deseja criar.");
            nomeficheiro = sc.nextLine();
        }
        System.out.println("\uD83D\uDCC1 Indique o tipo de ficheiro pretendido: ");
        String tipoficheiro = sc.nextLine();
        while (!tipoficheiro.equalsIgnoreCase(".txt") && !tipoficheiro.equalsIgnoreCase("txt")
                && !tipoficheiro.equalsIgnoreCase(".csv") && !tipoficheiro.equalsIgnoreCase("csv")) {
            System.out.println("ERRO: O tipo de ficheiro não é válido!");
            tipoficheiro = sc.nextLine();
        }
        if (tipoficheiro.equalsIgnoreCase(".txt") || tipoficheiro.equalsIgnoreCase(".csv")) {
            System.out.printf("O ficheiro: %s%s", nomeficheiro, tipoficheiro + " foi criado com sucesso!");
        }
        return nomeficheiro.concat(tipoficheiro);
    }

    public static void guardarFicheiro(String output, String[] imprimir) throws IOException {
        FileWriter fich = new FileWriter(new File(output), true);
        String linha = "";
        for (int i = 0; i < imprimir.length; i++) {
            if (i == 0) {
                linha += imprimir[i];
            } else {
                linha += "," + imprimir[i];
            }
        }
        fich.write(linha + "\n");

        fich.close();
    }

    public static void testesUnitarios() {
        System.out.println("Bem-vindo aos testes unitários!");
        if (ValidarData("2020-04-01", "yyyy-MM-dd") == 0 | ValidarData("2020-04-01", "yyyy-MM-dd") == 1) {
            System.out.println("ValidarData: WORKING");
        } else {
            System.out.println("ValidarData: NOT WORKING");
        }
    }
}