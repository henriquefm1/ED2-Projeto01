public class No implements Comparable<No>{
    char caractere;
    int frequencia;
    No esquerda, direita;

    //Constructor
    public No(char caractere, int frequencia){
        this.caractere = caractere;
        this.frequencia = frequencia;
        this.direita = null;
        this.esquerda = null;
    }

    @Override
    public int compareTo(No noAux){
        return this.frequencia - noAux.frequencia;
    }
}
