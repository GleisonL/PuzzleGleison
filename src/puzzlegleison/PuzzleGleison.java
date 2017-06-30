/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package puzzlegleison;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;

/**
 *
 * @author Gleison
 */
public class PuzzleGleison {
    
    static final byte [] objetivoPuzzle = { 1, 2, 3, 4, 5, 6, 7, 8, 0 };

    final PriorityQueue <estado> aberto = new PriorityQueue<estado>(100, new Comparator<estado>() {
        @Override
        public int compare(estado a, estado b) { 
            return a.prioridade()- b.prioridade();
        }
    });

    final HashSet <estado> fechado = new HashSet <estado>();
    
    class estado {
        final byte [] puzzle;    
        final int spaceIndex;   
        final int movimentos;            
        final int iHeuristica;            
        final estado prev;       

        
        int prioridade() {
            return movimentos + iHeuristica;
        }

        estado(byte [] inicial) {
            puzzle = inicial;
            spaceIndex = index(puzzle, 0);
            movimentos = 0;
            iHeuristica = heuristic(puzzle);
            prev = null;
        }


        estado(estado prev, int slideFromIndex) {
            puzzle = Arrays.copyOf(prev.puzzle, prev.puzzle.length);
            puzzle[prev.spaceIndex] = puzzle[slideFromIndex];
            puzzle[slideFromIndex] = 0;
            spaceIndex = slideFromIndex;
            movimentos = prev.movimentos + 1;
            iHeuristica = heuristic(puzzle);
            this.prev = prev;
        }

        
        boolean objetivo() {
            return Arrays.equals(puzzle, objetivoPuzzle);
        }

        
        estado moveA() { 
            return spaceIndex > 2 ? new estado(this, spaceIndex - 3) : null; 
        }       
        estado moveB() { 
            return spaceIndex < 6 ? new estado(this, spaceIndex + 3) : null; 
        }       
        estado moveC() { 
            return spaceIndex % 3 > 0 ? new estado(this, spaceIndex - 1) : null; 
        }       
        estado moveD() { 
            return spaceIndex % 3 < 2 ? new estado(this, spaceIndex + 1) : null; 
        }

        
        void print() {
            System.out.println("Prioridade escohida = " + prioridade()) ;
            System.out.println("-----------") ;
            System.out.println("Prioridade =  Movimentos + iHeuristica = " + movimentos + "+" + iHeuristica);
            System.out.println("-----------------------------------------------") ;
            for (int i = 0; i < 9; i += 3)
                System.out.println(puzzle[i] + " " + puzzle[i+1] + " " + puzzle[i+2]);
        }

        
        void imprimeTodas() {
            if (prev != null) prev.imprimeTodas();
            System.out.println();
            print();
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof estado) {
                estado other = (estado)obj;
                return Arrays.equals(puzzle, other.puzzle);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(puzzle);
        }
    }

    
    void addSuccessor(estado sucessor) {
        if (sucessor != null && !fechado.contains(sucessor)) 
            aberto.add(sucessor);
    }

    
    void resolve(byte [] inicial) {

        aberto.clear();
        fechado.clear();

    
        long comeco = System.currentTimeMillis();

    
        aberto.add(new estado(inicial));
        while (!aberto.isEmpty()) {

    
            estado estado = aberto.poll();

            
            if (estado.objetivo()) {
                long tempo = System.currentTimeMillis() - comeco;
                estado.imprimeTodas();
                System.out.println("Tempo em milisegundos = " + tempo);
                return;
            }

            
            fechado.add(estado);

            
            addSuccessor(estado.moveA());
            addSuccessor(estado.moveB());
            addSuccessor(estado.moveC());
            addSuccessor(estado.moveD());
        }
    }

    
    static int index(byte [] a, int val) {
        for (int i = 0; i < a.length; i++)
            if (a[i] == val) return i;
        return -1;
    }

    
    static int manhattenDistance(int a, int b) {
        return Math.abs(a / 3 - b / 3) + Math.abs(a % 3 - b % 3);
    }

    
    static int heuristic(byte [] puzzle) {
        int iHeuristica= 0;
        for (int i = 0; i < puzzle.length; i++)
            if (puzzle[i] != 0)
                iHeuristica+= manhattenDistance(i, puzzle[i]);
        return iHeuristica;
    }

    public static void main(String[] args) {

        
        byte [] inicial = { 3,2,5,4,0,1,8,6,7};

        
        new PuzzleGleison().resolve(inicial);
    }
    
}
