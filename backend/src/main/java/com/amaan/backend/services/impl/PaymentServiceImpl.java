package com.amaan.backend.services.impl;

import com.amaan.backend.constants.FineStatus;
import com.amaan.backend.constants.PaymentProvider;
import com.amaan.backend.constants.PaymentStatus;
import com.amaan.backend.dtos.request.PaymentRequest;
import com.amaan.backend.dtos.response.PaymentResponse;
import com.amaan.backend.dtos.response.ReceiptResponse;
import com.amaan.backend.entity.*;
import com.amaan.backend.mappers.PaymentMapper;
import com.amaan.backend.repository.FineRepository;
import com.amaan.backend.repository.PaymentRepository;
import com.amaan.backend.security.userdetails.CustomUserDetails;
import com.amaan.backend.services.PaymentService;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final FineRepository fineRepository;
    //private final RazorpayClient razorpayClient;
    //private final RazorpayOrderGateway razorpayOrderGateway;
    private final PaymentMapper paymentMapper;

    public PaymentServiceImpl(
            PaymentRepository paymentRepository,
            FineRepository fineRepository,
            //RazorpayClient razorpayClient,
            //RazorpayOrderGateway razorpayOrderGateway,
            PaymentMapper paymentMapper) {

        this.paymentRepository = paymentRepository;
        this.fineRepository = fineRepository;
        //this.razorpayClient = razorpayClient;
        //this.razorpayOrderGateway = razorpayOrderGateway;
        this.paymentMapper = paymentMapper;
    }

    @Override
    @Transactional
    public PaymentResponse createOrder(
            PaymentRequest request,
            String idempotencyKey) {

        if (idempotencyKey == null || idempotencyKey.isBlank()) {
            throw new RuntimeException("Idempotency-Key is required");
        }

        Payment existingPayment =
                paymentRepository
                        .findByIdempotencyKey(idempotencyKey)
                        .orElse(null);

        if (existingPayment != null) {
            return paymentMapper.toResponse(existingPayment);
        }

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        User user = userDetails.getUser();

        Fine fine = fineRepository
                .findById(request.getFineId())
                .orElseThrow(() ->
                        new RuntimeException("Fine not found"));

        if (!fine.getBorrowRecord()
                .getUser()
                .getId()
                .equals(user.getId())) {

            throw new RuntimeException(
                    "You cannot pay this fine"
            );
        }

        if (fine.getStatus() == FineStatus.PAID) {
            throw new RuntimeException("Fine is already paid");
        }

        if (fine.getAmount() == null ||
                fine.getAmount().compareTo(BigDecimal.ZERO) <= 0) {

            throw new RuntimeException("Invalid fine amount");
        }

        try {

            long amountInPaise =
                    fine.getAmount()
                            .movePointRight(2)
                            .longValueExact();

            String receipt = "fine_" + fine.getId();

            String orderId = "order_mock_" + java.util.UUID.randomUUID();

            Payment payment = Payment.builder()
                    .fine(fine)
                    .user(user)
                    .provider(PaymentProvider.RAZORPAY)
                    .providerOrderId(orderId)
                    .amount(fine.getAmount())
                    .currency("INR")
                    .status(PaymentStatus.CREATED)
                    .idempotencyKey(idempotencyKey)
                    .build();

            paymentRepository.saveAndFlush(payment);

            return paymentMapper.toResponse(payment);

        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to create Razorpay order",
                    e
            );
        }
    }


    @Override
    @Transactional
    public PaymentResponse choosePaymentOption(
            java.util.UUID paymentId,
            com.amaan.backend.dtos.request.PaymentChoiceRequest request
    ) {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        User user = userDetails.getUser();

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() ->
                        new RuntimeException("Payment not found"));

        if (!payment.getUser().getId().equals(user.getId())) {
            throw new RuntimeException(
                    "You cannot access this payment");
        }

        Fine fine = payment.getFine();

        if (fine.getStatus() == FineStatus.PAID) {
            throw new RuntimeException("Fine is already paid");
        }

        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            throw new RuntimeException("Payment is already successful");
        }

        switch (request.choice()) {
            case PAY_LATER -> {
                payment.setStatus(PaymentStatus.PENDING);
            }

            case PAY_NOW -> {
                payment.setStatus(PaymentStatus.SUCCESS);
                payment.setPaidAt(java.time.Instant.now());

                fine.setStatus(FineStatus.PAID);
                fine.setPaidAt(java.time.Instant.now());

                fineRepository.save(fine);
            }
        }

        paymentRepository.save(payment);

        return paymentMapper.toResponse(payment);
    }



    @Override
    @Transactional
    public ReceiptResponse getReceipt(java.util.UUID paymentId) {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        CustomUserDetails userDetails =
                (CustomUserDetails) authentication.getPrincipal();

        User user = userDetails.getUser();

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() ->
                        new RuntimeException("Payment not found"));

        if (!payment.getUser().getId().equals(user.getId())) {
            throw new RuntimeException(
                    "You cannot access this receipt");
        }

        Fine fine = payment.getFine();
        BorrowRecord borrowRecord = fine.getBorrowRecord();
        BookCopy bookCopy = borrowRecord.getBookCopy();
        Book book = bookCopy.getBook();

        return new ReceiptResponse(
                "RCP-" + payment.getId(),
                payment.getId(),
                fine.getId(),

                user.getId(),

                book.getId(),
                bookCopy.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getIsbn(),

                fine.getAmount(),
                payment.getCurrency(),
                fine.getDaysOverdue(),
                fine.getStatus(),
                payment.getStatus(),

                borrowRecord.getBorrowDate(),
                borrowRecord.getDueDate(),
                borrowRecord.getReturnDate(),
                payment.getCreatedAt(),
                payment.getPaidAt()
        );
    }


}