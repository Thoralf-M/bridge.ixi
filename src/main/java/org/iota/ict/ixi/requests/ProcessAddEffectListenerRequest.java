package org.iota.ict.ixi.requests;

import org.iota.ict.eee.EffectListenerQueue;
import org.iota.ict.eee.Environment;
import org.iota.ict.ixi.ClientHandler;
import org.iota.ict.ixi.protobuf.Request;

public class ProcessAddEffectListenerRequest {

    public static void process(Request.AddEffectListenerRequest request, ClientHandler clientHandler) {

        clientHandler.addEffectListener(new EffectListenerQueue<>(new Environment(request.getEnvironment())));

    }

}
