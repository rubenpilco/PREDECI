/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package recursos;
//
/**
 *
 * @author USER
 */
    public class ValidaRucJuridicos_Extranjeros {

    /**
    * @param args
    */
    private static  final int num_provincias = 24;
    //public static String rucPrueba = “1790011674001″;
    private static int[] coeficientes = {4,3,2,7,6,5,4,3,2};
    private static int constante = 11;

    public static Boolean validacionRUC(String ruc){
//    verifica que los dos primeros digitos correspondan a un valor entre 1 y NUMERO_DE_PROVINCIAS
    int prov = Integer.parseInt(ruc.substring(0, 2));

    if (!((prov > 0) && (prov <= num_provincias))) {
    System.out.println("Error: ruc ingresada mal");
    return false;
    }

    //verifica que el último dígito de la cédula sea válido
    int[] d = new int[10];
    int suma = 0;

    //Asignamos el string a un array
    for (int i = 0; i < d.length; i++) {
    d[i] = Integer.parseInt(ruc.charAt(i) + "");
    }

    for (int i=0; i< d.length - 1; i++) {
    d[i] = d[i] * coeficientes[i];
    suma += d[i];
    //System.out.println(“Vector d en ” + i + ” es ” + d[i]);
    }

    System.out.println("Suma es: " + suma);

    int aux, resp;

    //aux = suma % constante;
    aux = suma % constante;
    if(aux==0)
    {resp=0;}
    else
    {resp = constante - aux;}

    //resp = constante - aux;


    //resp = (resp == 10) ? 0 : resp;

    System.out.println("Aux: " + aux);
    System.out.println("Resp " + resp);
    System.out.println("d[9] " + d[9]);

    if (resp == d[9]) {
    return true;
    }
    else
    return false;
    }

    public static void main(String[] args) {
    String ruc_dato = "1191709779001";
    //String ruc_dato = "0660001250001";
    if (validacionRUC(ruc_dato))
    System.out.println("El RUC es correcto");
    else
    System.out.println("El RUC es incorrecto");
    }
    }
