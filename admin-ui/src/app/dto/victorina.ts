import {Question} from "./question";

export class Victorina {

  constructor(public id: unknown, public name: string, public title: string,
              public questions: Question[], public ownerId: string,
              public winnerId: string) {

  }

  toString():string{
    return `Victorina[name=${this.name}; title=${this.title}; question=${this.questions}]`
  }

}
