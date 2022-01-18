GraalJS-NB Parser
=================

This is a copy of the graal-js repository. An early version of the graal-js
parser was used in NetBeans as the parser for javascript files. When the source
code for NetBeans, the JS parser was not included, but graal-js was relicensed
to UPL.

The new code was retrofitted by Jaroslav Tulach, so that effectively the old
parser is available under UPL. The original relicensing can be found in commit
e20fe15dc14a42b5246901cefc524235e7b77505.

This branch removed all code, not relevant for the parser and makes it available
as a maven project.