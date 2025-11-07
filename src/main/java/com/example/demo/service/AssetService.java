package com.example.demo.service;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.asset.EvaluateRequest;
import com.example.demo.dto.asset.AssetHistoryResponse;
import com.example.demo.dto.asset.AssetRequest;
import com.example.demo.dto.asset.AssetResponse;
import com.example.demo.entity.Asset;
import com.example.demo.entity.AssetHistory;
import com.example.demo.entity.AssetType;
import com.example.demo.entity.User;
import com.example.demo.enums.AssetHistoryAction;
import com.example.demo.enums.AssetStatus;
import com.example.demo.enums.NotificationType;
import com.example.demo.exception.DataNotFound;
import com.example.demo.entity.Notification;
import com.example.demo.entity.Department;
import com.example.demo.repository.AssetHistoryRepository;
import com.example.demo.repository.AssetRepository;
import com.example.demo.repository.AssetTypeRepository;
import com.example.demo.repository.NotificationRepository;
import com.example.demo.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AssetService {
        private final AssetRepository assetRepository;
        private final AssetTypeRepository assetTypeRepository;
        private final UserRepository userRepository;
        private final AssetHistoryRepository assetHistoryRepository;
        private final NotificationRepository notificationRepository;

        public void create(AssetRequest assetRequest) {
                AssetType type = assetTypeRepository.findById(assetRequest.getTypeId())
                                .orElseThrow(() -> new DataNotFound("Asset type not found"));

                User user = null;
                if (assetRequest.getAssignedTo() != 0) {
                        user = userRepository.findById(assetRequest.getAssignedTo())
                                        .orElseThrow(() -> new DataNotFound("User not found"));
                }

                Asset asset = Asset.builder().code(assetRequest.getCode())
                                .name(assetRequest.getName())
                                .type(type)
                                .assignedTo(user)
                                .purchaseDate(assetRequest.getPurchaseDate())
                                .value(assetRequest.getValue())
                                .status(assetRequest.getStatus())
                                .condition(assetRequest.getCondition())
                                .description(assetRequest.getDescription())
                                .build();
                assetRepository.save(asset);

                AssetHistory history = AssetHistory.builder()
                                .asset(asset)
                                .actionType(AssetHistoryAction.CREATED)
                                .performedAt(Instant.now())
                                .performedBy(user)
                                .details(String.format("Created asset %d (%s)", asset.getId(), asset.getName()))
                                .previousStatus(asset.getStatus())
                                .newStatus(asset.getStatus())
                                .build();
                assetHistoryRepository.save(history);

        }

        public void update(Long id, AssetRequest assetRequest) {
                Asset asset = assetRepository.findById(id)
                                .orElseThrow(() -> new DataNotFound("Asset not found"));

                AssetType type = assetTypeRepository.findById(assetRequest.getTypeId())
                                .orElseThrow(() -> new DataNotFound("Asset type not found"));

                User user = null;
                if (assetRequest.getAssignedTo() != 0) {
                        user = userRepository.findById(assetRequest.getAssignedTo())
                                        .orElseThrow(() -> new DataNotFound("User not found"));
                }

                asset.setCode(assetRequest.getCode());
                asset.setName(assetRequest.getName());
                asset.setType(type);
                asset.setAssignedTo(user);
                asset.setPurchaseDate(assetRequest.getPurchaseDate());
                asset.setValue(assetRequest.getValue());
                asset.setStatus(assetRequest.getStatus());
                asset.setCondition(assetRequest.getCondition());
                asset.setDescription(assetRequest.getDescription());

                assetRepository.save(asset);

                AssetHistory history = AssetHistory.builder()
                                .asset(asset)
                                .actionType(AssetHistoryAction.UPDATED)
                                .performedAt(Instant.now())
                                .performedBy(user)
                                .details(String.format("Updated asset %d (%s)", asset.getId(), asset.getName()))
                                .previousStatus(asset.getStatus())
                                .newStatus(asset.getStatus())
                                .build();
                assetHistoryRepository.save(history);
        }

        public void delete(Long id) {
                Asset asset = assetRepository.findById(id)
                                .orElseThrow(() -> new DataNotFound("Asset not found"));
                assetRepository.delete(asset);
                AssetHistory history = AssetHistory.builder()
                                .asset(asset)
                                .actionType(AssetHistoryAction.DELETED)
                                .performedAt(Instant.now())
                                .details(String.format("Deleted asset %d (%s)", asset.getId(), asset.getName()))
                                .previousStatus(asset.getStatus())
                                .newStatus(null)
                                .build();
                assetHistoryRepository.save(history);
        }

        public AssetResponse getById(Long id) {
                Asset asset = assetRepository.findById(id)
                                .orElseThrow(() -> new DataNotFound("Asset not found"));

                AssetType type = assetTypeRepository.findById(id)
                                .orElseThrow(() -> new DataNotFound("Asset type not found"));

                return AssetResponse.builder()
                                .id(asset.getId())
                                .code(asset.getCode())
                                .name(asset.getName())
                                .typeId(type.getId())
                                .assignedTo(asset.getAssignedTo() == null ? null : asset.getAssignedTo().getId())
                                .departmentId(asset.getAssignedTo() == null ? null
                                                : asset.getAssignedTo().getDepartment().getId())
                                .purchaseDate(asset.getPurchaseDate())
                                .value(asset.getValue())
                                .status(asset.getStatus())
                                .condition(asset.getCondition())
                                .description(asset.getDescription())
                                .createdBy(asset.getCreatedBy() == null ? null : asset.getCreatedBy().getId())
                                .createdAt(asset.getCreatedAt())
                                .build();
        }

        public List<AssetResponse> getAll() {
                List<Asset> assets = assetRepository.findAll();
                return assets.stream().map(asset -> AssetResponse.builder()
                                .id(asset.getId())
                                .code(asset.getCode())
                                .name(asset.getName())
                                .typeId(asset.getType().getId())
                                .assignedTo(asset.getAssignedTo() == null ? null : asset.getAssignedTo().getId())
                                .departmentId(asset.getAssignedTo() == null ? null
                                                : asset.getAssignedTo().getDepartment().getId())
                                .purchaseDate(asset.getPurchaseDate())
                                .value(asset.getValue())
                                .status(asset.getStatus())
                                .condition(asset.getCondition())
                                .description(asset.getDescription())
                                .createdBy(asset.getCreatedBy() == null ? null : asset.getCreatedBy().getId())
                                .createdAt(asset.getCreatedAt())
                                .build()).toList();
        }

        public void assign(Long assetId, Long userId) {
                Asset asset = assetRepository.findById(assetId)
                                .orElseThrow(() -> new DataNotFound("Asset not found"));

                User user = userRepository.findById(userId)
                                .orElseThrow(() -> new DataNotFound("User not found"));

                AssetStatus previousStatus = asset.getStatus();

                asset.setAssignedTo(user);
                asset.setStatus(AssetStatus.IN_USE);

                assetRepository.save(asset);

                AssetHistory history = AssetHistory.builder()
                                .asset(asset)
                                .actionType(AssetHistoryAction.ASSIGNED)
                                .performedAt(Instant.now())
                                .performedBy(user)
                                .details(String.format("Assigned to user %d (%s)", user.getId(), user.getName()))
                                .previousStatus(previousStatus)
                                .newStatus(asset.getStatus())
                                .build();
                assetHistoryRepository.save(history);

                // Tạo notification cho user được assign
                Notification notification = Notification.builder()
                                .user(user)
                                .asset(asset)
                                .title("Tài sản đã được gán cho bạn")
                                .message(String.format("Tài sản %s (%s) đã được gán cho bạn. Mã tài sản: %s",
                                                asset.getName(), asset.getType().getName(), asset.getCode()))
                                .type(NotificationType.INFO)
                                .isRead(false)
                                .build();
                notificationRepository.save(notification);

                // Tạo notification cho manager của department (nếu có)
                if (user.getDepartment() != null) {
                        Department department = user.getDepartment();
                        if (department.getManagerId() != null) {
                                User manager = userRepository.findById(department.getManagerId())
                                                .orElse(null);
                                if (manager != null) {
                                        Notification managerNotification = Notification.builder()
                                                        .user(manager)
                                                        .asset(asset)
                                                        .title("Tài sản đã được gán cho nhân viên trong phòng ban")
                                                        .message(String.format(
                                                                        "Tài sản %s (%s) đã được gán cho %s trong phòng ban %s. Mã tài sản: %s",
                                                                        asset.getName(), asset.getType().getName(),
                                                                        user.getName(), department.getName(),
                                                                        asset.getCode()))
                                                        .type(NotificationType.INFO)
                                                        .isRead(false)
                                                        .build();
                                        notificationRepository.save(managerNotification);
                                }
                        }
                }
        }

        public void revoke(Long assetId) {
                Asset asset = assetRepository.findById(assetId)
                                .orElseThrow(() -> new DataNotFound("Asset not found"));

                User currentUser = asset.getAssignedTo();
                if (currentUser == null) {
                        throw new DataNotFound("Asset is not currently assigned to any user");
                }

                AssetStatus previousStatus = asset.getStatus();

                asset.setAssignedTo(null);
                asset.setStatus(AssetStatus.IN_STOCK);

                assetRepository.save(asset);

                AssetHistory history = AssetHistory.builder()
                                .asset(asset)
                                .actionType(AssetHistoryAction.RECLAIMED)
                                .performedAt(Instant.now())
                                .performedBy(currentUser)
                                .details(String.format("Assignment reclaimed from user %d (%s)", currentUser.getId(),
                                                currentUser.getName()))
                                .previousStatus(previousStatus)
                                .newStatus(asset.getStatus())
                                .build();
                assetHistoryRepository.save(history);

                // Tạo notification cho user bị thu hồi
                Notification notification = Notification.builder()
                                .user(currentUser)
                                .asset(asset)
                                .title("Tài sản đã được thu hồi")
                                .message(String.format("Tài sản %s (%s) đã được thu hồi từ bạn. Mã tài sản: %s",
                                                asset.getName(), asset.getType().getName(), asset.getCode()))
                                .type(NotificationType.WARNING)
                                .isRead(false)
                                .build();
                notificationRepository.save(notification);
        }

        public List<AssetHistoryResponse> getAllAssetHistory() {
                List<AssetHistory> histories = assetHistoryRepository.findAllByOrderByPerformedAtAsc();
                return histories.stream().map(history -> AssetHistoryResponse.builder()
                                .id(history.getId())
                                .assetId(history.getAsset().getId())
                                .actionType(history.getActionType().name())
                                .performedBy(history.getPerformedBy() == null ? null : history.getPerformedBy().getId())
                                .performedAt(history.getPerformedAt().toString())
                                .details(history.getDetails())
                                .notes(history.getNotes())
                                .previousStatus(history.getPreviousStatus() == null ? null
                                                : history.getPreviousStatus().name())
                                .newStatus(history.getNewStatus() == null ? null : history.getNewStatus().name())
                                .build()).toList();
        }

        public void evaluate(Long assetId, EvaluateRequest assetHistoryRequest) {
                User user = userRepository.findById(assetHistoryRequest.getPerformedBy())
                                .orElseThrow(() -> new DataNotFound("User not found"));
                Asset asset = assetRepository.findById(assetId)
                                .orElseThrow(() -> new DataNotFound("Asset not found"));

                asset.setCondition(assetHistoryRequest.getCondition());
                assetRepository.save(asset);

                AssetHistory history = AssetHistory.builder()
                                .asset(asset)
                                .actionType(AssetHistoryAction.EVALUATED)
                                .performedAt(Instant.now())
                                .performedBy(user)
                                .details(String.format("Evaluated asset id %d (%s)", asset.getId(), asset.getName()))
                                .previousStatus(asset.getStatus())
                                .newStatus(asset.getStatus())
                                .notes(assetHistoryRequest.getNotes())
                                .build();
                assetHistoryRepository.save(history);
        }
}
