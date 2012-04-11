package org.scalaquery.ql.basic

import java.sql.PreparedStatement
import org.scalaquery.MutatingStatementInvoker
import org.scalaquery.ql.Query
import org.scalaquery.session.{PositionedParameters, PositionedResult}
import org.scalaquery.util.RecordLinearizer

class BasicQueryTemplate[P, R](query: Query[_, R], profile: BasicProfile) extends MutatingStatementInvoker[P, R] {

  protected lazy val sres = profile.buildSelectStatement(query)

  def selectStatement = getStatement

  protected def getStatement = sres.sql

  protected def setParam(param: P, st: PreparedStatement): Unit = sres.setter(new PositionedParameters(st), param)

  protected def extractValue(rs: PositionedResult): R = sres.linearizer.narrowedLinearizer.asInstanceOf[RecordLinearizer[R]].getResult(profile, rs)

  protected def updateRowValues(rs: PositionedResult, value: R) = sres.linearizer.narrowedLinearizer.asInstanceOf[RecordLinearizer[R]].updateResult(profile, rs, value)
}
