import java.util.ArrayList;

public class MinHeap{
    private ArrayList<No> heap;

    public MinHeap(){
        this.heap = new ArrayList<>();
    }

    public int tamanho(){
        return heap.size();
    }

    //inserir frequencia na arvore
    public int inserir(No no, int valor){
        if(no != null){ //verifica se a arvore existe
            if(valor < no.valor){ // verifica se o valor inserido eh menor q o valor do no, se sim, vai pra esquerda
                if(no.esq != null){ //se existir n o na esquerda, ele continua a busca com a recursao
                    inserir(no.esq, valor);
                }else{ //se nao for nulo, insere na esquerda
                    no.esq = new No(valor);
                }//repete a logica para a direita
            }else if( valor > no.valor){
                if(no.dir != null){
                    inserir(no.dir, valor);
                }
            }else{
                no.dir = new No(valor);
            }
        }
    }


}
