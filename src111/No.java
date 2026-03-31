public class No implements Comparable<No> {
    char caractere;
    int frequencia;
    No esquerda, direita;

    //construtor
    public No(char caractere, int frequencia) {
        this.caractere = caractere;
        this.frequencia = frequencia;
        this.direita = null;
        this.esquerda = null;
    }

    @Override
    public int compareTo(No outroNo) {
        int diffFreq = this.frequencia - outroNo.frequencia;
        if (diffFreq != 0) {
            return diffFreq;
        }
        
        return this.caractere - outroNo.caractere;
    }
}
