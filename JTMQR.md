# JTM Query Result (JTMQR) #

The JSON result set for a query is based on the [Tuple Sequence Encoding](http://astma.it.bond.edu.au/tmip-specification.dbk?section=6) of the _"TMIP, Topic Map Interaction Protocol 0.3, Specification"_ by Robert Barta. The following format simply tries to map the RelaxNG description to JSON with some minor changes:

  * the XTM 1.0 fragments will be replaced by [JTM 1.0](http://www.cerny-online.com/jtm/1.0/) (or [JTM 1.1](http://www.cerny-online.com/jtm/1.1/)).
  * the **xmlns** namespace definition will be skipped by now
  * **id** for a tuple is not supported