/**
 * 
 */
package thesis.nlp.util;

import edu.stanford.nlp.trees.TypedDependency;
import thesis.nlp.core.process.StanfordTreeTypedDependConsts;

/**
 * @author lohuynh
 *
 */
public class TypedDependencyCheckUtil {
	public static boolean checkATagIsAVerb(String tag) {
		if (tag.equals("") || tag == null)
			return false;
		switch (tag) {
		case StanfordTreeTypedDependConsts.VB:
			return true;
		case StanfordTreeTypedDependConsts.VBD:
			return true;
		case StanfordTreeTypedDependConsts.VBN:
			return true;
		case StanfordTreeTypedDependConsts.VBZ:
			return true;
		case StanfordTreeTypedDependConsts.VBP:
			return true;
		case StanfordTreeTypedDependConsts.VBG:
			return true;

		default:
			return false;
		}
	}
	
	public static boolean checkATagIsAnAdj(String tag){
		if (tag.equals("") || tag == null)
			return false;
		switch (tag) {
		case StanfordTreeTypedDependConsts.JJ:
			return true;
		case StanfordTreeTypedDependConsts.JJR:
			return true;
		default:
			return false;
		}
	}
	
	public static boolean checkATagIsAnNoun(String tag){
		if (tag.equals("") || tag == null)
			return false;
		switch (tag) {
		case StanfordTreeTypedDependConsts.NN:
			return true;
		case StanfordTreeTypedDependConsts.NNS:
			return true;
		case StanfordTreeTypedDependConsts.NNP:
			return true;
		case StanfordTreeTypedDependConsts.NNPS:
			return true;
		case "$":
			return true;
		default:
			return false;
		}
	}
	
	public static boolean checkParticleInVerb(String particle, TypedDependency tdl)
	{
		if (particle == null)
			return false;
		if (particle.equals(""))
			return true;
		boolean istrue = false;
		if (tdl.reln().getSpecific() != null)
		{
			if (!tdl.reln().getSpecific().toUpperCase().equals(particle.toUpperCase()))
				return true;
		}
		return istrue;
	}
	
	public static boolean checkAuxRel(TypedDependency tdl)
	{
		String reln = tdl.reln().getShortName();
		if (reln.equals(StanfordTreeTypedDependConsts.AUX) || reln.equals(StanfordTreeTypedDependConsts.AUXPASS))
		{
			return true;
		}
		else 
			return false;
	}
	
	public static boolean checkRel(TypedDependency tdl, String rel)
	{
		String reln = tdl.reln().getShortName();
		if (reln.equals(rel))
		{
			return true;
		}
		else 
			return false;
	}
	
	
	
	
}
