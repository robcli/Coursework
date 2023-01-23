class FSA{
  constructor(){
    let poss = [];
    let state = undefined;
    // this.getPoss = () => poss;
    //showState() => String || undefined
    this.showState = () => state === undefined ? undefined: state.getName();
    //renameState (oldName: String, newName: String): this
    this.renameState = (oldName, newName) => {
      poss.forEach(s => {if(s.getName() === oldName){s.setName(newName);}});
      return this;
    }
    //pullTrans(nState: State, a: Transition[])   takes a state and an array of transitions and adds them to the state
    let pullTrans = (nState, a) => {
      a.forEach(t => {
        let n = Object.keys(t)[0];
        let v = lib220.getProperty(t, n).value;
        if(!poss.some(state => v === state.getName())){
          this.createState(v, []);
        }
        nState.addTransition(n, poss[poss.findIndex(state => state.getName() === v)]);
      });
    }
    // createState(s: string, transitions: Transition[]): this
    this.createState = (s, transitions) => {
      let nState = new State(s);
      if (poss.length === 0){poss.push(nState); state = nState;} //if first state it becomes the curState
      let index = poss.findIndex(state => state.getName() === s); //finds out if it exists and if so its index
      if (index === -1) {
        poss.push(nState);
      } else {
        poss[index].deleteTransitions(); //replacing the old state
      }
      pullTrans(index === -1 ? nState : poss[index], transitions); //throws either the new state or an old state to add transitions
      return this;
    }
    //addTransition(s: string, t: Transition) adds transition (t) to state named s
    this.addTransition = (s, t) => {
      if(!poss.some(state => {
        if(state.getName() === s){
          pullTrans(state, [t]); return true;} //if found state is found it'll add the transitions
        else{return false;}})){
        this.createState(s, [t]); //else it needs to create the state
      } 
      return this;
    }
    // nextState(e: string): this 
    this.nextState = (e) => {
      if(state !== undefined){
        state = state.nextState(e);}
      return this;
    }
    //createMemento(): Memento
    this.createMemento = () => {let m = new Memento(); m.storeState(this.showState()); return m;}
    //restoreMemento(m: Memento): this
    this.restoreMemento = (m) => {
      poss.forEach(s => {
        if(s.getName() === m.getState()){state = s;} 
      });
      return this;
    }
    class State{
      constructor(name){
        let value = name;
        let t = [];
        // this.getT = () => t;
        //getName(): String
        this.getName = () => value;
        //setName(s: string): this
        this.setName = (s) => {value = s; return this;};
        //addTransition(e: string, s: State): this
        this.addTransition = (e, s) => {
          let temp = {};
          lib220.setProperty(temp, e, s);
          t.push(temp);
          return this;
        }
        //deleteTransitions(): 
        this.deleteTransitions = () => {t = []};
        //nextState(e: string): State
        this.nextState = (e) =>{
          let random = (min, max) => Math.floor(Math.random() * (max - min) + min);
          let po = this.nextStates(e);
          if(po.length === 0){return undefined;}
          return po[random(0, po.length)]; 
        }
        // nextStates(e: string): State[]
        this.nextStates = (e) =>{
          return t.reduce((acc, ts) => Object.keys(ts)[0] === e ? acc.concat([lib220.getProperty(ts, e).value]) : acc, []);
        }
        Object.freeze(this);
      }
    }

    class Memento{
      constructor(){
        let state = undefined;
        // storeState(s: string): void
        this.storeState = (s) => {state = s;}; 
        // getState(): string
        this.getState = () => state;
        Object.freeze(this);
      }
    }
  Object.freeze(this);
  }
}
