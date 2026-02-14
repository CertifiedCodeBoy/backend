package org.example.backend.service;

import org.example.backend.entity.Transaction;
import org.example.backend.entity.TransactionLine;
import org.example.backend.entity.User;

/**
 * Service for Picking operations.
 * Picking = retrieving products from storage and placing into picking racks.
 * Stock OUT from source (storage), stock IN at destination (picking rack).
 */
public interface PickingService {

    /**
     * Process a single picking line.
     */
    void processPickingLine(Transaction transaction, TransactionLine line, User performer);

    /**
     * Process all lines of a picking transaction.
     */
    void processPicking(Transaction transaction, User performer);
}
