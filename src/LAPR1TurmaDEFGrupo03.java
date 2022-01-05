import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;


public class LAPR1TurmaDEFGrupo03 {
    static final String File = "nome_do_ficheiro.csv";

    public static void main(String[] args) throws FileNotFoundException {
        try {                                                                                                                //CATCH CORRE MESMO QUANDO O TRY ESTÁ EXECUTÁVEL
            String r = args[1];
            String di = args[3];
            String df = args[5];
            String in = args[6];
            String out = args[7];
            int[][] matriz = Scann();                                                                                       // Se estiver só este, não dá erro mas não dá print na matriz.
        } catch (Exception e) {
            System.out.println("Modo Iterativo.");

        }
    }

    //public static final String delimiter = ",";                                                                           //AJUDA! NÃO ESTÁ A FUNCIONAR
    public static int[][] Scann() throws FileNotFoundException {
        Scanner input = new Scanner(new File("nome_do_ficheiro.csv"));
        int[][] matrix = new int[10][10];
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                matrix[i][j] = input.nextInt();
                System.out.println(matrix[i][j]);                                                                           // Só coloquei o print para ver como funcionava o código.
            }
        }
        input.close();
        return matrix;
    }
}
