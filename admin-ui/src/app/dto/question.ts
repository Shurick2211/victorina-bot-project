export class Question {


  constructor(public text:string , public answers:Array<string>, public rightAnswer:number) {

  }
  toString():string{
    return `Question[text=${this.text}; rightAnswer=${this.rightAnswer}; answers=${this.answers.toString()}]`
  }
}
