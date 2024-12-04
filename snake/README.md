Design:
- Divide screen into units of 8 such that the playable space becomes a 64x32 grid.
- Since the snake has to move but also grow we create and leverage a Queue data structure. 
    - Each node represents a block in (x, y) coordinates of the snakes body. 
    - To move the snake we: 1. push a new node containing the new heads position, and 2. pop the tail node. This provides the ilusion that the snake is moving.
    - To grow the snake after it eats the food we move the head without poping the tail node.

- Development approach (incrementally added features):
    1. A line that *grows* in the direction of the currently pressed key.
    2. A line or dot that *moves* in the direction of the currently pressed key

- Additional features:
    - Movements are sam
    - 
With the jack compiler that comes with the Software Suite constructor arguments can not be the same name as names from an object variable.

E.g. the following cause all the field variables x and y to be initialized to zero.
```
class Node {
    field int x, y;
    field Node next;

    constructor Node new(**int x, int y**) {
        **let x = x;**
        **let y = y;**
        let next = null;
        return this;
    }

    ...
}

```
Instead do:
```
class Node {
    field int x, y;
    field Node next;

    constructor Node new(**int arg_x, int arg_y**) {
        **let x = arg_x;**
        **let y = arg_y;**
        let next = null;
        return this;
    }

    ...
}
```