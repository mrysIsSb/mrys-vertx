package top.mrys.auth.jwt;

import cn.hutool.crypto.KeyUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.signers.AlgorithmUtil;
import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;
import java.security.KeyPair;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mrys
 * @date 2021/7/15
 */
public class JWTTool{

  static String id = "RS256";


  public static String create(Map<String, Object> payload, KeyPair keyPair) {
    JWTSigner signer = JWTSignerUtil.createSigner(id, keyPair);
    return JWTUtil.createToken(payload, signer);
  }

  public static JSONObject get(String token, PublicKey publicKey) {
    JWT jwt = JWT.of(token)
        .setSigner(JWTSignerUtil.rs256(publicKey));
    if (jwt.verify()) {
      return jwt.getPayloads();
    }
    return null;
  }

  public static void main(String[] args) {
    KeyPair keyPair = KeyUtil.generateKeyPair(AlgorithmUtil.getAlgorithm(id));
    HashMap<String, Object> payload = new HashMap<>();
    payload.put("hello", "yijie");
    payload.put("hello1", 123123);
    System.out.println();
    String s = create(payload, keyPair);
    System.out.println(s);
    System.out.println(get(s, KeyUtil.generateRSAPublicKey(keyPair.getPublic().getEncoded())));
  }
}
