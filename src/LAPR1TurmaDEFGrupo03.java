import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;


public class LAPR1TurmaDEFGrupo03 {

    public static void main(String[] args) throws FileNotFoundException{
        int[][] matriz = new int[5][5];
        try{                                                                                                                //CATCH CORRE MESMO QUANDO O TRY ESTÁ EXECUTÁVEL
            String r = args[1];
            String di = args[3];
            String df = args[5];
            String in = args[6];
            String out = args[7];
            matriz= Scann();
        }catch (Exception e) {
            System.out.println("Modo Iterativo.");
            matriz= Scann();

        }
    }

    //public static final String delimiter = ",";                                                                           //AJUDA! NÃO ESTÁ A FUNCIONAR
    public static int[][] Scann() throws FileNotFoundException{
        Scanner sc = new Scanner(new File("nome_do_ficheiro.csv"));
        int[][] matrix = new int[5][5];
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                matrix[i][j] = Integer.parseInt(sc.next());
            }
        }
        sc.close();
        return matrix;
    }
}
