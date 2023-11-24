import {Quiz} from "./quiz";


export class Person {
  constructor(
    public id:string,
    public firstName:String,
    public lastName:String,
    public userName:string,
    public languageCode:string,
    public channelsAdmin:Array<String>,
    public quizes:Array<Quiz>,
    public password:String
  ) {

  }

}
