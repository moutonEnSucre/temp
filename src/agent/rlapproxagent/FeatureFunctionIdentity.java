package agent.rlapproxagent;

import environnement.Action;
import environnement.Etat;

/**
 * Vecteur de fonctions caracteristiques phi_i(s,a): autant de fonctions caracteristiques que de paire (s,a),
 * <li> pour chaque paire (s,a), un seul phi_i qui vaut 1  (vecteur avec un seul 1 et des 0 sinon).
 * <li> pas de biais ici
 * @author laetitiamatignon
 */
public class FeatureFunctionIdentity implements FeatureFunction {
	//*** VOTRE CODE
	private int nbEtat, nbAction;
	
	public FeatureFunctionIdentity(int _nbEtat, int _nbAction){
		//*** VOTRE CODE
		this.nbEtat = _nbEtat;
		this.nbAction = _nbAction;
	}
	
	@Override
	public int getFeatureNb() {
		//*** VOTRE CODE
		return 0;
	}

	@Override
	public double[] getFeatures(Etat e,Action a){
		//*** VOTRE CODE
		
		return null;
	}
	

}
