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
                modoInterativo();
                Scann();
                break;
            case 1:
                System.out.println("Modo não interativo");
                break;
        }
    }

    public static int verificacaoModo(String[] args) {
        int count = args.length;
        if (count == 0) return 0;
        else return 1;
    }

    public static void modoInterativo() throws FileNotFoundException {
        Scanner sc = new Scanner(System.in);
        String nomeFile;
        nomeFile = sc.nextLine();
        int res = resolucaoInterface();
        System.out.println("Indique o nome do ficheiro fonte: ");
        System.out.println("Indique a data de inicio: ");
        int [] di = recolhaData();
        System.out.println("Indique a data final: ");
        int [] df = recolhaData();

    }
    public static int[] recolhaData() {
        Scanner sc = new Scanner(System.in);
        int[] data = new int[3];
        System.out.println("Dia: ");
        data[0] = sc.nextInt();
        System.out.println("Mes: ");
        data[1] = sc.nextInt();
        System.out.println("Ano: ");
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
        } return matrix;
    } }