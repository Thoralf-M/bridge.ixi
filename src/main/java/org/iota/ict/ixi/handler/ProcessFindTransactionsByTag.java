package org.iota.ict.ixi.handler;

import com.google.protobuf.ByteString;
import org.iota.ict.ixi.protobuf.Model;
import org.iota.ict.ixi.protobuf.Request;
import org.iota.ict.ixi.protobuf.Response;
import org.iota.ict.ixi.protobuf.Wrapper;
import org.iota.ict.model.transaction.Transaction;

import java.io.IOException;
import java.util.Set;

public class ProcessFindTransactionsByTag {

    public static void process(Request.FindTransactionsByTagRequest request, ClientHandler clientHandler) throws IOException {

        Set<Transaction> transactions = clientHandler.getIxi().findTransactionsByTag(request.getTag());

        if(transactions.size() == 0) {
            Wrapper.WrapperMessage wrapperMessage = Wrapper.WrapperMessage.newBuilder()
                    .setMessageType(Wrapper.WrapperMessage.MessageType.FIND_TRANSACTIONS_BY_TAG_RESPONSE)
                    .build();
            clientHandler.getOutputStream().writeInt(wrapperMessage.toByteArray().length);
            wrapperMessage.writeTo(clientHandler.getOutputStream());
            return;
        }

        Response.FindTransactionsByTagResponse.Builder responseBuilder = Response.FindTransactionsByTagResponse.newBuilder();

        for(Transaction transaction: transactions) {

            Model.Transaction ret = Model.Transaction.newBuilder()
                    .setSignatureFragments(transaction.signatureFragments())
                    .setExtraDataDigest(transaction.extraDataDigest())
                    .setAddress(transaction.address())
                    .setValue(ByteString.copyFrom(transaction.value.toByteArray()))
                    .setIssuanceTimestamp(transaction.issuanceTimestamp)
                    .setTimelockLowerBound(transaction.timelockLowerBound)
                    .setTimelockUpperBound(transaction.timelockUpperBound)
                    .setBundleNonce(transaction.bundleNonce())
                    .setTrunkHash(transaction.trunkHash())
                    .setBranchHash(transaction.branchHash())
                    .setTag(transaction.tag())
                    .setAttachmentTimestamp(transaction.attachmentTimestamp)
                    .setAttachmentTimestampLowerBound(transaction.attachmentTimestampLowerBound)
                    .setAttachmentTimestampUpperBound(transaction.attachmentTimestampUpperBound)
                    .setNonce(transaction.nonce())
                    .setDecodedSignatureFragments(transaction.decodedSignatureFragments())
                    .setEssence(transaction.essence())
                    .setIsBundleHead(transaction.isBundleHead)
                    .setIsBundleTail(transaction.isBundleTail)
                    .build();

            responseBuilder.addTransaction(ret);

        }

        Wrapper.WrapperMessage wrapperMessage = Wrapper.WrapperMessage.newBuilder()
                .setMessageType(Wrapper.WrapperMessage.MessageType.FIND_TRANSACTIONS_BY_TAG_RESPONSE)
                .setFindTransactionsByTagResponse(responseBuilder.build())
                .build();

        clientHandler.getOutputStream().writeInt(wrapperMessage.toByteArray().length);
        wrapperMessage.writeTo(clientHandler.getOutputStream());

    }

}
