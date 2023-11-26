import {Question} from "./question";

export class Victorina {

  constructor(public id: unknown, public name: string, public title: string,
              public questions: Question[], public ownerId: string, public chanelName:string | null,
              public winnerId: string | null, public startDate: string | null, public endDate: string | null) {

  }

  toString():string{
    return `Victorina[name=${this.name}; title=${this.title}; question=${this.questions};
    startDate=${this.startDate}; endDate=${this.endDate}]`
  }

}
