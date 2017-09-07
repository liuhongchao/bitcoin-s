package me.hongchao.bitcoin4s.script

sealed trait ReservedOp extends ScriptOpCode

object ReservedOp {
  case object OP_RESERVED extends ReservedOp { val value = 80 }
  case object OP_VER extends ReservedOp { val value = 98 }
  case object OP_VERIF extends ReservedOp { val value = 101 }
  case object OP_VERNOTIF extends ReservedOp { val value = 102 }
  case object OP_RESERVED1 extends ReservedOp { val value = 137 }
  case object OP_RESERVED2 extends ReservedOp { val value = 138 }
  case object OP_NOP1 extends ReservedOp { val value = 176 }
  case object OP_NOP4 extends ReservedOp { val value = 179 }
  case object OP_NOP5 extends ReservedOp { val value = 180 }
  case object OP_NOP6 extends ReservedOp { val value = 181 }
  case object OP_NOP7 extends ReservedOp { val value = 182 }
  case object OP_NOP8 extends ReservedOp { val value = 183 }
  case object OP_NOP9 extends ReservedOp { val value = 184 }
  case object OP_NOP10 extends ReservedOp { val value = 185 }

  val all = Seq(
    OP_RESERVED, OP_VER, OP_VERIF, OP_VERNOTIF, OP_RESERVED1, OP_RESERVED2,
    OP_NOP1, OP_NOP4, OP_NOP5, OP_NOP6, OP_NOP7, OP_NOP8, OP_NOP9, OP_NOP10
  )

  implicit val interpreter = new Interpreter[ReservedOp] {
    def interpret(opCode: ReservedOp, context: InterpreterContext): InterpreterContext = {
      opCode match {
        case OP_NOP1 | OP_NOP4 | OP_NOP5 | OP_NOP6 | OP_NOP7 | OP_NOP8 | OP_NOP9 | OP_NOP10 =>
          context.copy(opCount = context.opCount + 1)
        case OP_RESERVED | OP_VER | OP_RESERVED1 | OP_RESERVED2 =>
          throw NotExecutableReservedOpcode(opCode, context.stack)
        case OP_VERIF | OP_VERNOTIF =>
          // These two OpCodes should be checked before script is executed. If found, entire
          // transaction should be invalid.
          throw InValidReservedOpcode(opCode, context.stack)
      }
    }
  }
}