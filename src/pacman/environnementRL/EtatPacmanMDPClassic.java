package pacman.environnementRL;

import pacman.elements.StateGamePacman;
import environnement.Etat;

/**
 * Classe pour définir un etat du MDP pour l'environnement pacman avec QLearning tabulaire
 */
public class EtatPacmanMDPClassic implements Etat , Cloneable{

	int positionX;
	int positionY;
	int posXGhost;
	int posYGhost;
	int closeDot;
	
	public EtatPacmanMDPClassic(StateGamePacman _stategamepacman){
		positionX = _stategamepacman.getPacmanState(0).getX();
		positionY = _stategamepacman.getPacmanState(0).getY();
		posXGhost = _stategamepacman.getGhostState(0).getX();
		posYGhost = _stategamepacman.getGhostState(0).getY();
		closeDot = _stategamepacman.getClosestDot(_stategamepacman.getPacmanState(0));
	}
	
	@Override
	public String toString() {
		
		return "";
	}
	
	
	public Object clone() {
		EtatPacmanMDPClassic clone = null;
		try {
			// On recupere l'instance a renvoyer par l'appel de la 
			// methode super.clone()
			clone = (EtatPacmanMDPClassic)super.clone();
		} catch(CloneNotSupportedException cnse) {
			// Ne devrait jamais arriver car nous implementons 
			// l'interface Cloneable
			cnse.printStackTrace(System.err);
		}

		// on renvoie le clone
		return clone;
	}
}
