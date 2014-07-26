DepChain-java
=============

FCA dependency heuristics implemented using Colibri-java.
Based on paper "Formal concept analysis of disease similarity"
[[PMC3392047](http://www.ncbi.nlm.nih.gov/pmc/articles/PMC3392047/)], however,
chains are constructed from co-atoms to atoms and no bound is used.

##What it does

This code was originally developed to explore dependencies in artificial data
given as observation tables for experiments with PNDRI.
These tables have unlabeled rows and columns.
Each row corresponds to a subject/sample, and each row to a column.
An entry gives the observed value of the variable for the subject/sample.
Such a table is converted to an incidence relation where samples are objects, and
variable-value pairs are attributes.
It is also possible to read object-attribute pairs into the incidence relation.

The search heuristic follows chains from co-atoms to atoms.
The co-atoms will generally be individual attributes, though it may be determined
by sets of redundant attributes (incident on the same objects).
Chains are followed by selecting the subconcept with the largest cardinality
object set until an atom is reached.

The output is a table of concepts along the dependency chains listing
attributes,
number of objects,
partial object entropy (from global frequency),
partial object entropy (from Jaccard coefficient),
partial attribute entropy (from global frequency), and
partial attribute entropy (from Jaccard coefficient).

##What it doesn't do

FCA mechanisms from Colibri-Java are used, and there is no reduction of the
  incidence relation.

The disease dependency paper used a threshold on the heuristic that Felix Eichinger
and I just pulled out of a hat based on the chronic kidney disease data set.
The threshold has no basis in any theory.
This code comes from a collaboration with Nikita Sakhanenko and David Galas at
PNDRI relating dependencies in the formal concept lattice to their
information theoretic measures.
Nikita has code, I have code, but the punchline is that
neither of us know when to stop.
So at the moment, I am dumping all of the concepts along each elaborated chain.

By default, it also does not save the chain, it just dumps concepts as it reaches them.
The dot output will save the chains in dot format, but not give the partial entropies.

##Building
This repository is an STS Maven project, and the JCommander dependency should be
handled by Maven if you clone the repository and build.

However, the code depends on Colibri-Java, which is a
Google summer-of-code project developed by Daniel Goetzmann that lives
[here](https://code.google.com/p/colibri-java/).
My code uses a couple utility classes in colibri.lib (noted in noted in
ConceptSearch.java) that do not have public accessibility in the repository version.

Unfortunately, I was lazy, and remain that way, and just reached inside and
changed the access modifiers.
You should be able to easily import Colibri-Java as a project into STS/Eclipse.
You'll then have to fix the accessibility issues by hand –
it is probably easiest to let the compiler/Eclipse find them for you.
*I have replaced the main culprit to get the Dot output working correctly, but haven't
checked whether it works with a clean copy of Colibri-Java.*

##Disclaimer

This is a research tool under construction, and orphanhood is inevitable.

The code is "as is" and has no warranty.
