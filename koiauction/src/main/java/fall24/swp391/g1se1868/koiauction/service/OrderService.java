package fall24.swp391.g1se1868.koiauction.service;

import fall24.swp391.g1se1868.koiauction.model.*;
import fall24.swp391.g1se1868.koiauction.repository.AuctionRepository;
import fall24.swp391.g1se1868.koiauction.repository.OrderRepository;
import fall24.swp391.g1se1868.koiauction.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private AuctionRepository auctionRepository;

    @Autowired
    private UserRepository userRepository;

    public Order addOrder(Integer auctionId, OrderRequest orderRequest, Integer userId) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new RuntimeException("Auction not found"));
        if(auction.getFinalPrice()==null||auction.getWinnerID()==null||!auction.getStatus().equalsIgnoreCase("Closed")){
            throw new RuntimeException("Auction status is not closed");
        }
        if(auction.getWinnerID()!=userId){
            throw new RuntimeException("User is not the winner ID");
        }
        User bidder = userRepository.findById(auction.getWinnerID())
                .orElseThrow(() -> new RuntimeException("Bidder not found"));
        Order order = new Order();
        order.setAuctionID(auction);
        order.setBidderID(bidder);
        order.setAddress(orderRequest.getAddress());
        order.setDate(Instant.now());
        order.setPrice(auction.getFinalPrice());
        order.setPhoneNumber(orderRequest.getPhoneNumber());
        order.setNote(orderRequest.getNote());
        order.setFullName(orderRequest.getFullName());
        order.setStatus("Pending");
        return orderRepository.save(order);
    }
    public List<OrderResponse> getOrdersByUser(Integer userId) {
        List<Order> ordersAsBidder = orderRepository.findOrdersByBidderId(userId);
        List<Order> ordersAsBreeder = orderRepository.findOrdersByBreederId(userId);

        List<Order> combinedOrders = new ArrayList<>();
        combinedOrders.addAll(ordersAsBidder);
        combinedOrders.addAll(ordersAsBreeder);

        return combinedOrders.stream().map(order -> new OrderResponse(
                order.getId(),
                order.getBidderID() != null ? order.getBidderID().getId() : null,
                order.getAuctionID() != null ? order.getAuctionID().getId() : null,
                order.getAddress(),
                order.getDate(),
                order.getPrice(),
                order.getPhoneNumber(),
                order.getNote(),
                order.getStatus()
        )).toList();
    }
}
