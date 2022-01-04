import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;


public class LAPR1TurmaDEFGrupo03 {

    public static void main(String[] args) throws FileNotFoundException{
        try{
            r=args[1];
            di=args[3];
            df=args[5];
            in=args[6];
            out=args[7];
            int[][] matriz= Scann();
        }catch (Exception e) {
            System.out.println("Modo Iterativo.");
            int[][] matriz= Scann();
        }
    }

    public static final String delimiter = ",";                                                                          //AJUDA!
    public static int[][] Scann() throws FileNotFoundException{
        Scanner sc = new Scanner(new File("nome_do_ficheiro"));
        int[][] matrix;
        for (int i = 0; i < ; i++) {
            for (int j = 0; j < ; j++) {
                matrix[i][j] = Integer.parseInt(sc.next());
            }
        }
        sc.close();
        return matrix;
    }
}
