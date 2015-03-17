# Nesting #

Implementing nested conversations, or sub-flows, is fairly simple.

Nested conversations are achieved in one of three ways:  inheritance, explicit declaration, and an optional package-based nesting feature.

## Sections ##
  1. [Package-Based Nesting (the best approach)](#Package-Based_Nesting.md)
  1. [Explicit Declaration](#Explicit_Declaration.md)
  1. [Inheritance](#Inheritance.md)


---

### Package-Based Nesting ###
Package-based nesting allows for "magical" nesting of conversations, but it is actually the most powerful, simplest, and best way to achieve nested conversations.

Basically, its like this:  actions located in sub-packages are sub-conversations of actions located higher in the package tree.

Package-based nesting is turned off by default.  To turn it on, add this to your struts.xml:
```
<constant name="conversation.package.nesting" value="true" />
```



---

### Explicit Declaration ###

```
@ConversationController({"main","some","ordering"})
public class SomeController {

   
   @BeginConversation("some") 
   public String begin() {return "success";}

   public String blahAction() {return "success";}

   @EndConversation("some") 
   public String end() {return "success";}

}
```

Here, all three actions are members of the three conversations declared in the @ConversationController annotation.  But the begin and end actions only begin and end the "some" conversation.



---

### Inheritance ###

The conversation inheritance mechanism works like this:  conversations in a sub-class are sub-conversations, and "begin" and "end" actions only begin and end the conversations of the classes in which they are declared.   Since that may sound a little confusing at first, consider the following two Struts2 action classes:

```
@ConversationController
public abstract class SuperController {

   public String beginSuper() {return "success";}
   public String someSuperAction() {return "success";}
   public String endSuper() {return "success";}

}
```

```
@ConversationController
public class SubController extends SuperController {

   public String beginSub() {return "success";}
   public String someSubAction() {return "success";}
   public String endSub() {return "success";}

}
```

In the above, all 6 of the actions are valid "members" of both the Super and the Sub conversations.  But beginSuper only begins a Super conversation, beginSub only begins a sub conversation, endSuper only ends a Super conversation, and endSub only ends a sub conversation.

By "members", we mean to say that the actions will keep the conversations alive.  So by all six actions being members of both conversations, then the active Super and Sub conversations will stay alive for any of the actions, but the actions are only started and ended as specified above.