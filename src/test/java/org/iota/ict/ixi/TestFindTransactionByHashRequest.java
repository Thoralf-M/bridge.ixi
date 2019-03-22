package org.iota.ict.ixi;

import org.iota.ict.ixi.protobuf.Model;
import org.iota.ict.ixi.protobuf.Request;
import org.iota.ict.ixi.protobuf.Wrapper;
import org.iota.ict.ixi.util.Constants;
import org.iota.ict.model.transaction.Transaction;
import org.iota.ict.model.transaction.TransactionBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.net.Socket;

public class TestFindTransactionByHashRequest extends TestTemplate {

    @Test
    public void testFindTransactionByHashRequest() throws IOException, InterruptedException {

        // send transaction from Ict2 to Ict1
        Transaction transaction = new TransactionBuilder().build();
        ict2.submit(transaction);
        Thread.sleep(500);

        // register external module to bridge
        Socket socket = new Socket("127.0.0.1", Constants.DEFAULT_BRIDGE_PORT);

        // request transaction by hash
        Request.FindTransactionByHashRequest request = Request.FindTransactionByHashRequest.newBuilder().setHash(transaction.hash).build();
        Wrapper.WrapperMessage message = Wrapper.WrapperMessage.newBuilder()
                .setMessageType(Wrapper.WrapperMessage.MessageType.FIND_TRANSACTION_BY_HASH_REQUEST)
                .setFindTransactionByHashRequest(request)
                .build();

        message.writeDelimitedTo(socket.getOutputStream());

        // read response from bridge
        Wrapper.WrapperMessage response = Wrapper.WrapperMessage.parseDelimitedFrom(socket.getInputStream());
        Model.Transaction protobuf = response.getFindTransactionByHashResponse().getTransaction();

        Assert.assertEquals(transaction.hash, protobuf.getHash());

    }

}
