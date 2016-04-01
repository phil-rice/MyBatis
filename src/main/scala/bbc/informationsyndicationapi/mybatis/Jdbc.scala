package bbc.informationsyndicationapi.mybatis

import java.sql.{Connection, Statement}

import org.apache.commons.dbcp2.BasicDataSource

trait JbdcConfig{
  def username: String
  def password: String
  def ur: String
  def driverClassName: String

}

//trait Jdbc {
//
//  def jdbcConfig: JbdcConfig
//
//  val dataSource = new BasicDataSource()
//  dataSource.setUsername(jdbcConfig.username)
//  dataSource.setPassword(jdbcConfig.username)
//  dataSource.setUrl(jdbcConfig.username)
//  dataSource.setDriverClassName(jdbcConfig.driverClassName)
//
//  def withConnection[X](connectionFn: Connection => X) ={
//    val connection = dataSource.getConnection
//    try{
//      connectionFn(connection)
//    } finally {
//      connection.close
//    }
//  }
//
//  def withStatement[X](statementFn: Statement => X)(implicit connection: Connection) =
//    withConnection{connection =>
//      val statement = connection.createStatement()
//      try{
//        statementFn(statement)
//      }finally{
//        statement.close
//      }
//  }
//
//  def executeSql(sql: String) = withStatement(statement => statement.execute(sql))
//
//def updateVersion(versionTableName: String,  versionColumn: String) ={
//  executeSql(s"update  $versionTableName set $versionColumn = $versionColumn +1")
//}



//}
