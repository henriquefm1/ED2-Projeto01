public class Main {
    public static void main(String[] args) {
        //testa a função com a palavra do exemplo no pdf
        String textoTeste = "BANANA";
        int[] resultado = contarFrequencias(textoTeste);

        //imprime apenas os caracteres que apareceram se a frequencia for maior que 0
        System.out.println("ETAPA 1: Tabela de Frequencia de Caracteres");
        for (int i = 0; i < resultado.length; i++) {
            if (resultado[i] > 0) {
                System.out.println("Caractere '" + (char)i + "' (ASCII: " + i + "): " + resultado[i]);
            }
        }
    }

    public static int[] contarFrequencias(String texto){

    //cria um vetor onde todas as 356 posições começam com 0
        int[] frequencias = new int[256];

        //passa por cada letra do texto
        for(int i = 0; i < texto.length(); i++){
            char caractere = texto.charAt(i);

            //o caractere vira o indice do vetor, soma +1 naquela posição
            frequencias[caractere]++;
        }
        return frequencias;
    }
}
