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
import java.util.List;

public class TestFindTransactionByTagRequest extends TestTemplate {

    @Test
    public void testFindTransactionByHashRequest() throws IOException, InterruptedException {

        // tag to test
        String tag = "TEST9TAG9999999999999999999";

        // send transaction from Ict2 to Ict1
        TransactionBuilder transactionBuilder1 = new TransactionBuilder();
        TransactionBuilder transactionBuilder2 = new TransactionBuilder();
        transactionBuilder1.tag = tag;
        transactionBuilder2.tag = tag;

        Transaction transaction1 = transactionBuilder1.build();
        Transaction transaction2 = transactionBuilder2.build();

        ict2.submit(transaction1);
        ict2.submit(transaction2);
        Thread.sleep(500);

        // register external module to bridge
        Socket socket = new Socket("127.0.0.1", Constants.DEFAULT_BRIDGE_PORT);

        // request transaction by hash
        Request.FindTransactionsByTagRequest request = Request.FindTransactionsByTagRequest.newBuilder().setTag(tag).build();
        Wrapper.WrapperMessage message = Wrapper.WrapperMessage.newBuilder()
                .setMessageType(Wrapper.WrapperMessage.MessageType.FIND_TRANSACTIONS_BY_TAG_REQUEST)
                .setFindTransactionsByTagRequest(request)
                .build();

        message.writeDelimitedTo(socket.getOutputStream());

        // read response from bridge
        Wrapper.WrapperMessage response = Wrapper.WrapperMessage.parseDelimitedFrom(socket.getInputStream());
        List<Model.Transaction> transactions = response.getFindTransactionsByTagResponse().getTransactionList();

        Assert.assertEquals(2, transactions.size());

    }

}
