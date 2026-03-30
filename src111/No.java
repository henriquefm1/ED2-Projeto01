public class No implements Comparable<No> {
    char caractere;
    int frequencia;
    No esquerda, direita;

    // Construtor
    public No(char caractere, int frequencia) {
        this.caractere = caractere;
        this.frequencia = frequencia;
        this.direita = null;
        this.esquerda = null;
    }

    @Override
    public int compareTo(No outroNo) {
        // Primeiro critério: Menor frequência
        int diffFreq = this.frequencia - outroNo.frequencia;
        if (diffFreq != 0) {
            return diffFreq;
        }
        
        // Segundo critério (DESEMPATE VITAL): Ordem alfabética / ASCII
        // Isso garante que a árvore sempre será montada exatamente igual.
        return this.caractere - outroNo.caractere;
    }
}