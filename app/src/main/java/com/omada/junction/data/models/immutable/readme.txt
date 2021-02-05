
This package holds classes that are to be used when performing writes over the network. It is not
the best solution so it will probably be refactored later if it is deemed unsafe or verbose or convoluted.

No changes can be made whatsoever to any instance of these classes so instances are guaranteed to be
thread safe unless any class attribute is mutable and something is holding a reference to it externally
Like a map or array list for example. Handle this case separately in the constructor when needed
by performing a deep copy.