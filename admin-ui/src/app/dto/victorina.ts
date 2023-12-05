import {Question} from "./question";
import {Channel} from "./channel";

export class Victorina {

  constructor(public id: unknown,
              public name: string,
              public title: string,
              public questions: Question[],
              public ownerId: string,
              public channel: Channel | null,
              public winnerId: string | null,
              public rightsAnsweredUserId: Array<String>|null,
              public isManyAnswer:boolean,
              public numberParticipants:number,
              public isActive:boolean,
              public hasPrize:Boolean,
              public startDate: Date | null,
              public endDate: Date | null) {

  }

  toString():string{
    return `Victorina[name=${this.name}; title=${this.title}; Cannel=${this.questions};
    startDate=${this.startDate}; endDate=${this.endDate}]`
  }

}
