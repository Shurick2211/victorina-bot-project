export class Quiz {

  constructor(
    public victorinaId:String,
    public userAnswers:Array<number>,
    public isWinner:Boolean
  ) {
  }
}
