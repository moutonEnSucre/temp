package agent.planningagent;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import environnement.*;

/**
 * Cet agent met a jour sa fonction de valeur avec value iteration 
 * et choisit ses actions selon la politique calculee.
 * @author laetitiamatignon
 */
public class ValueIterationAgent extends PlanningValueAgent{
	/**
	 * discount facteur
	 */
	protected double gamma;

	/**
	 * fonction de valeur des etats
	 */
	protected HashMap<Etat,Double> V;
	
	/**
	 * @param gamma
	 * @param mdp
	 */
	public ValueIterationAgent(double gamma,  MDP mdp) {
		super(mdp);
		this.gamma = gamma;
		
		this.V = new HashMap<Etat,Double>();
		for (Etat etat:this.mdp.getEtatsAccessibles()){
			V.put(etat, 0.0);
		}
	}
	
	public ValueIterationAgent(MDP mdp) {
		this(0.9,mdp);
	}
	
	/**
	 * Mise a jour de V: effectue UNE iteration de value iteration (calcule V_k(s) en fonction de V_{k-1}(s'))
	 * et notifie ses observateurs.
	 * Ce n'est pas la version inplace (qui utilise la nouvelle valeur de V pour mettre a jour ...)
	 */
	@Override
	public void updateV(){
		//delta est utilise pour detecter la convergence de l'algorithme
		//Dans la classe mere, lorsque l'on planifie jusqu'a convergence, on arrete les iterations        
		//lorsque delta < epsilon 
		//Dans cette classe, il  faut juste mettre a jour delta 
		this.delta=0.0;
		//*** VOTRE CODE
		HashMap<Etat,Double> Vk = new HashMap<>(V);
		double vmax = Double.MIN_VALUE, vmin = Double.MAX_VALUE;

		for(Etat t: V.keySet()){
			if(!mdp.estAbsorbant(t)){
				double max_sum = Double.NEGATIVE_INFINITY;
				for (Action a: mdp.getActionsPossibles(t)) {
					double sum = 0;
					try {
						Map<Etat, Double> listTransitions = mdp.getEtatTransitionProba(t, a);
						for(Etat t_prime: listTransitions.keySet()) {
							double recompense = mdp.getRecompense(t, a, t_prime);
							double value = Vk.get(t_prime);
							sum += listTransitions.get(t_prime) * (recompense + gamma * value);
						}
						if (max_sum < sum)
							max_sum = sum;
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (sum > max_sum)
						max_sum = sum;
				}
				if (max_sum > Double.NEGATIVE_INFINITY)
					V.replace(t, max_sum);
				if(max_sum > vmax)
					vmax = max_sum;
				if (max_sum < vmin)
					vmin = max_sum;
				double v = Math.abs(V.get(t) - Vk.get(t));
				if (delta < v)
					delta = v;
			}
		}
		//mise a jour de vmax et vmin utilise pour affichage du gradient de couleur:
		//vmax est la valeur max de V pour tout s 
		//vmin est la valeur min de V pour tout s
		this.vmin = vmin;
		this.vmax = vmax;
		
		//******************* laisser cette notification a la fin de la methode	
		this.notifyObs();
	}

	/**
	 * renvoi l'action executee par l'agent dans l'etat e 
	 * Si aucune actions possibles, renvoi Action2D.NONE
	 */
	@Override
	public Action getAction(Etat e) {
		//*** VOTRE CODE
		if(!mdp.getActionsPossibles(e).isEmpty()){
			return mdp.getActionsPossibles(e).get(0);
		} else
			return Action2D.NONE;
	}

	@Override
	public double getValeur(Etat _e) {
		//Renvoie la valeur de l'Etat _e, c'est juste un getter, ne calcule pas la valeur ici
		//la valeur est calculee dans updateV
		return V.get(_e);
	}
	/**
	 * renvoi action(s) de plus forte(s) valeur(s) dans etat 
	 * (plusieurs actions sont renvoyees si valeurs identiques, liste vide si aucune action n'est possible)
	 */
	@Override
	public List<Action> getPolitique(Etat _e) {
		//*** VOTRE CODE
		// retourne action de meilleure valeur dans _e selon V, 
		// retourne liste vide si aucune action legale (etat absorbant)
		List<Action> returnactions = new ArrayList<Action>();

			if(!mdp.estAbsorbant(_e)){
				double max_sum = Double.NEGATIVE_INFINITY;
				double sum = 0;
				for (Action a: mdp.getActionsPossibles(_e)) {
					try {
						Map<Etat, Double> listTransitions = mdp.getEtatTransitionProba(_e, a);
						for(Etat t_prime: listTransitions.keySet()) {
							double recompense = mdp.getRecompense(_e, a, t_prime);
							double value = V.get(t_prime);
							sum += listTransitions.get(t_prime) * (recompense + gamma * value);
						}
						if (max_sum < sum)
							max_sum = sum;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				for (Action action : mdp.getActionsPossibles(_e)){
					try {
						Map<Etat, Double> listTransitions = mdp.getEtatTransitionProba(_e, action);
						for(Etat t_prime: listTransitions.keySet()) {
							double recompense = mdp.getRecompense(_e, action, t_prime);
							double value = V.get(t_prime);
							sum += listTransitions.get(t_prime) * (recompense + gamma * value);
						}
						if (sum == max_sum)
							returnactions.add(action);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			else
				return new ArrayList<Action>();
		return returnactions;
	}
	
	@Override
	public void reset() {
		super.reset();
		//reinitialise les valeurs de V
		V.replace(mdp.getEtatInit(), 0d);
		this.notifyObs();
	}

	public HashMap<Etat,Double> getV() {
		return V;
	}
	public double getGamma() {
		return gamma;
	}

	@Override
	public void setGamma(double _g){
		System.out.println("gamma= "+gamma);
		this.gamma = _g;
	}

}
