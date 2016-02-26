package org.scalacoin.protocol.script

import org.scalacoin.marshallers.script.{RawScriptSignatureParser, ScriptParser}
import org.scalacoin.script.constant.ScriptToken
import org.scalacoin.util.ScalacoinUtil

/**
 * Created by chris on 1/19/16.
 * Responsible for the instantiation of ScriptSignature objects
 */
object ScriptSignatureFactory {


  /**
   * Creates a ScriptSignature object from a given hexadecimal script
   * @param hex
   * @return
   */
  def factory(hex : String) : ScriptSignature = {
    factory(ScalacoinUtil.decodeHex(hex))
  }

  /**
   * Creates a ScriptSignature object from a given list of bytes
   * @param bytes
   * @return
   */
  def factory(bytes : List[Byte]) : ScriptSignature = factory(bytes.toSeq)

  /**
   * Creates a ScriptSignature object from a given list of bytes
   * @param bytes
   * @return
   */
  def factory(bytes : Seq[Byte]) : ScriptSignature = RawScriptSignatureParser.read(bytes)

  /**
   * Builds a script signature from a scriptPubKye and a digital signature
   * @param scriptPubKey
   * @param signature
   */
  def factory(scriptPubKey: ScriptPubKey, signature : String) : ScriptSignature = factory(scriptPubKey,ScalacoinUtil.decodeHex(signature))

  /**
   * Builds a script signature from a scriptPubKye and a digital signature
   * @param scriptPubKey
   * @param signature
   */
  def factory(scriptPubKey : ScriptPubKey, signature : Seq[Byte]) : ScriptSignature = {
    ???
  }
  /**
   * Returns an empty script signature
   * @return
   */
  def empty = ScriptSignatureImpl(Seq(),"")
}
