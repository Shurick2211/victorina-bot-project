import {Quiz} from "./quiz";
import {PersonRole} from "../utils/person-role";


export class Person {
  constructor(
    public id:string,
    public firstName:String,
    public lastName:String,
    public userName:string,
    public languageCode:string,
    public channelsAdmin:Array<String>,
    public quizes:Array<Quiz>,
    public role:PersonRole,
    public password:String
  ) {

  }

}
