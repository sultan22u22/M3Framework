/*******************************************************************************
    Machine to Machine Measurement (M3) Framework 
    Copyright(c) 2012 - 2015 Eurecom

    M3 is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.


    M3 is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with M3. The full GNU General Public License is 
   included in this distribution in the file called "COPYING". If not, 
   see <http://www.gnu.org/licenses/>.

  Contact Information
  M3 : gyrard__at__eurecom.fr, bonnet__at__eurecom.fr, karima.boudaoud__at__unice.fr
  
The M3 framework has been designed and implemented during Amelie Gyrard's thesis.
She is a PhD student at Eurecom under the supervision of Prof. Christian Bonnet (Eurecom) and Dr. Karima Boudaoud (I3S-CNRS/University of Nice Sophia Antipolis).
This work is supported by the Com4Innov platform of the Pole SCS and DataTweet (ANR-13-INFR-0008). 
  
  Address      : Eurecom, Campus SophiaTech, 450 Route des Chappes, CS 50193 - 06904 Biot Sophia Antipolis cedex, FRANCE

 *******************************************************************************/
package eurecom.sparql.result;

import java.util.ArrayList;


import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.QuerySolutionMap;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;

import eurecom.common.util.WSUtils;

/**
 * To refactor according to the generic SPARQL query
 * @author Amelie Gyrard
 *
 */
public class ExecuteSparqlHealth extends ExecuteSparqlGeneric{

	public ExecuteSparqlHealth(Model model, String sparqlRequest) {
		super(model, sparqlRequest);
	}

	public ArrayList<SparqlResultRecipeNaturopathy> getSparqlResult(ArrayList<VariableSparql> var){
		QueryExecution qe = replaceVariableInRequest(this.model, query, var);
		//get result from sparql request
		ResultSet results = qe.execSelect() ;
		
		QuerySolution soln = null;
		ArrayList<SparqlResultRecipeNaturopathy> res = new ArrayList<SparqlResultRecipeNaturopathy>();
		SparqlResultRecipeNaturopathy sparqlResult = null;

		for ( ; results.hasNext() ; ){
			sparqlResult = new SparqlResultRecipeNaturopathy();
			soln = results.nextSolution() ;  

			if (soln.getResource("subject")!=null) {
				sparqlResult.setUri(soln.getResource("subject").getURI());
			}
			if (soln.getResource("seasonUri")!=null) {
				sparqlResult.setSeasonUri(soln.getResource("seasonUri").getURI());
			}
			if (soln.getLiteral("name")!= null) {
				sparqlResult.setName(soln.get("name").toString());
			}	
			if (soln.getLiteral("value")!= null) {
				String r = soln.get("value").toString();
				System.out.println(Double.valueOf(deleteTypeXsd(r)));
				sparqlResult.setValue(Double.valueOf(deleteTypeXsd(soln.get("value").toString())));
			}	
			if (soln.getLiteral("time")!= null) {
				sparqlResult.setTime(deleteTypeXsd(soln.get("time").toString()));
			}
			if (soln.getLiteral("season")!= null) {	
				sparqlResult.setSeason(deleteEndLabel(soln.get("season").toString()));
			}	
			if (soln.getLiteral("unit")!= null) {
				sparqlResult.setUnits(deleteTypeXsd(soln.get("unit").toString()));
			}	

			res.add(sparqlResult);	
		}
		qe.close();
		return res;
	}

}
