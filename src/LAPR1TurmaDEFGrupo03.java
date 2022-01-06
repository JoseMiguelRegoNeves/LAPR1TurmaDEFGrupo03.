import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;


public class LAPR1TurmaDEFGrupo03 {
    static final String FILE1 = "textFile.txt";

    public static void main(String[] args) throws FileNotFoundException {
        int[][] matriz = new int[5][5];
        int MOD = verificacaoModo(args);
        int res;
        switch (MOD) {
            case 0:
                System.out.println("Modo interativo");
                Scann();
                modoInterativo();
                break;
            case 1:
                System.out.println("Modo não interativo");
                Scann();
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

    // public static int recolhaDatas () {                                                                                      AVISO!

    // }

    public static int[][] calculoDif(int[][] matrizDatas) {
        int[][] matrizDiferenca = new int[1][matrizDatas[0].length];
        for (int i = 1; i < matrizDatas[0].length; i++) {
            matrizDiferenca[0][i-1] = matrizDatas[1][i] - matrizDatas[0][i];
        }
        return matrizDiferenca;
    }

    public static void modoNaoInterativo(String[] args) throws FileNotFoundException {
        Scanner sc = new Scanner(System.in);
        String nomeFileIn, nomeFileOut;
        String res = args[1]; // java -jar nome_programa.jar -r X -di DD-MM-AAAA -df DD-MM-AAAA registoNumerosCovid19.csv nome_ficheiro_saida.txt
        nomeFileIn = args[6];
        nomeFileOut = args[7];
        String di = args[3]; // recolhaData();                                                                                   Recolha de data por parâmetro?
        String df = args[5]; // recolhaData();                                                                                   Recolha de data por parâmetro?

    }

    public static void modoInterativo() throws FileNotFoundException {
        Scanner sc = new Scanner(System.in);
        String nomeFileIn;
        int res = resolucaoInterface();
        System.out.println("Indique o nome do ficheiro fonte: ");
        nomeFileIn = sc.nextLine();
        System.out.println("Indique a data de inicio: ");
        int[] di = recolhaData();
        System.out.println("Indique a data final: ");
        int[] df = recolhaData();

    }


    public static int[] recolhaData() {
        Scanner sc = new Scanner(System.in);
        int[] data = new int[3];
        System.out.println("Ano: ");
        data[0] = sc.nextInt();
        System.out.println("Mes: ");
        data[1] = sc.nextInt();
        System.out.println("Dia: ");
        data[2] = sc.nextInt();
        return data;
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
                System.out.println("Introduza uma opcao valida");
                resolucao = sc.nextInt();
            } while (resolucao != 0 && resolucao != 1 && resolucao != 2);
        }
        return resolucao;
    }


    //public static final String delimiter = ",";                                                                           //AJUDA! NÃO ESTÁ A FUNCIONAR
    public static String[][] Scann() throws FileNotFoundException {
        Scanner sc = new Scanner(new File("textFile.txt"));
        String linha = sc.nextLine();
        String[][] matrix = new String[5][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                matrix[i][j] = String.valueOf(linha.split(","));
                System.out.print(matrix[i][j] + " ");
            }
            sc.close();
        }
        return matrix;
    }
}