package com.deivimotors.application.service;

import com.deivimotors.application.exceptions.PaymentException;
import com.deivimotors.application.exceptions.PaymentNotFoundException;
import com.deivimotors.application.exceptions.PaymentUnprocessableEntityException;
import com.deivimotors.application.ports.in.PaymentServiceInputPort;
import com.deivimotors.application.ports.out.PaymentRepositoryOutputPort;
import com.deivimotors.domain.Payment;
import com.deivimotors.domain.enums.PaymentStatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public class PaymentService implements PaymentServiceInputPort {
    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    private final PaymentRepositoryOutputPort repository;

    public PaymentService(PaymentRepositoryOutputPort repository) {
        this.repository = repository;
    }

    @Override
    public Payment create(UUID saleId) throws PaymentNotFoundException {

        var payment = repository.getBySaleId(saleId);

        if (payment.isPresent()) {
            return payment.get();
        }

        var newPayment = new Payment(saleId);
        String idPayment = repository.save(newPayment);

        logger.info("Payment created for sale: " + newPayment.getSaleId());

        return new Payment(
                idPayment,
                newPayment.getSaleId(),
                newPayment.getStatus()
        )
                ;
    }

    @Override
    public Payment findPaymentBySaleId(UUID saleId) throws PaymentNotFoundException {
        logger.info("Starting find for sale: id" + saleId);
        return repository.getBySaleId(saleId)
                .orElseThrow(() ->
                        new PaymentNotFoundException("Payment for sale with id: " + saleId + " not found"));
    }

    @Transactional
    @Override
    public void checkout(UUID saleId, PaymentStatusEnum status) throws PaymentException {
        var payment = findPaymentBySaleId(saleId);

        if (payment.getStatus().equals(PaymentStatusEnum.PAGAMENTO_APROVADO) ||
                payment.getStatus().equals(PaymentStatusEnum.PAGAMENTO_RECUSADO) )
        {
            throw  new PaymentUnprocessableEntityException(
                    "The payment cannot be changed, as it has already been finalized. Current payment status : " + payment.getStatus());
        }
        //TODO Implementar finalização da venda e Veiculo atualizar veiculo

        switch (status) {
            case AGUARDANDO_PAGAMENTO -> throw new PaymentUnprocessableEntityException(
                    "Invalid status sequence for this update. Requested status: " + status);
            case PAGAMENTO_APROVADO -> {
                Payment paymentUpdated = payment.approve();
                repository.save(paymentUpdated);
            }
            case PAGAMENTO_RECUSADO -> {
                var paymentUpdated = payment.reject();
                repository.save(paymentUpdated);
            }
            default -> throw new PaymentUnprocessableEntityException(
                    "The current sale status: " + payment.getStatus() + " does not allow checkout");
        }
    }
}
