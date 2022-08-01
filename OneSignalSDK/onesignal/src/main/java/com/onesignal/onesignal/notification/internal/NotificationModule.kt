package com.onesignal.onesignal.notification.internal

import com.onesignal.onesignal.core.internal.application.IApplicationService
import com.onesignal.onesignal.core.internal.common.time.ITime
import com.onesignal.onesignal.core.internal.preferences.IPreferencesService
import com.onesignal.onesignal.core.internal.service.IBootstrapService
import com.onesignal.onesignal.core.internal.service.IStartableService
import com.onesignal.onesignal.core.internal.service.ServiceBuilder
import com.onesignal.onesignal.notification.INotificationsManager
import com.onesignal.onesignal.notification.internal.badges.impl.BadgeCountUpdater
import com.onesignal.onesignal.notification.internal.data.INotificationQueryHelper
import com.onesignal.onesignal.notification.internal.data.impl.NotificationQueryHelper
import com.onesignal.onesignal.notification.internal.data.INotificationDataController
import com.onesignal.onesignal.notification.internal.data.impl.NotificationDataController
import com.onesignal.onesignal.notification.internal.summary.impl.NotificationSummaryManager
import com.onesignal.onesignal.notification.internal.receivereceipt.impl.ReceiveReceiptWorkManager
import com.onesignal.onesignal.notification.internal.bundle.impl.NotificationBundleProcessor
import com.onesignal.onesignal.notification.internal.permissions.impl.NotificationPermissionController
import com.onesignal.onesignal.notification.internal.restoration.impl.NotificationRestoreWorkManager
import com.onesignal.onesignal.notification.internal.bundle.INotificationBundleProcessor
import com.onesignal.onesignal.notification.internal.generation.*
import com.onesignal.onesignal.notification.internal.display.impl.NotificationDisplayer
import com.onesignal.onesignal.notification.internal.channels.impl.NotificationChannelManager
import com.onesignal.onesignal.notification.internal.generation.impl.NotificationGenerationProcessor
import com.onesignal.onesignal.notification.internal.limiting.impl.NotificationLimitManager
import com.onesignal.onesignal.notification.internal.open.impl.NotificationOpenedProcessor
import com.onesignal.onesignal.notification.internal.analytics.impl.FirebaseAnalyticsTracker
import com.onesignal.onesignal.notification.internal.analytics.IAnalyticsTracker
import com.onesignal.onesignal.notification.internal.analytics.impl.NoAnalyticsTracker
import com.onesignal.onesignal.notification.internal.badges.IBadgeCountUpdater
import com.onesignal.onesignal.notification.internal.channels.INotificationChannelManager
import com.onesignal.onesignal.notification.internal.display.INotificationDisplayBuilder
import com.onesignal.onesignal.notification.internal.display.INotificationDisplayer
import com.onesignal.onesignal.notification.internal.display.ISummaryNotificationDisplayer
import com.onesignal.onesignal.notification.internal.display.impl.NotificationDisplayBuilder
import com.onesignal.onesignal.notification.internal.display.impl.SummaryNotificationDisplayer
import com.onesignal.onesignal.notification.internal.generation.impl.NotificationGenerationWorkManager
import com.onesignal.onesignal.notification.internal.lifecycle.INotificationLifecycleService
import com.onesignal.onesignal.notification.internal.lifecycle.impl.NotificationLifecycleService
import com.onesignal.onesignal.notification.internal.limiting.INotificationLimitManager
import com.onesignal.onesignal.notification.internal.open.INotificationOpenedProcessor
import com.onesignal.onesignal.notification.internal.open.INotificationOpenedProcessorHMS
import com.onesignal.onesignal.notification.internal.open.NotificationOpenedProcessorHMS
import com.onesignal.onesignal.notification.internal.permissions.INotificationPermissionController
import com.onesignal.onesignal.notification.internal.receivereceipt.IReceiveReceiptProcessor
import com.onesignal.onesignal.notification.internal.receivereceipt.IReceiveReceiptWorkManager
import com.onesignal.onesignal.notification.internal.receivereceipt.impl.ReceiveReceiptProcessor
import com.onesignal.onesignal.notification.internal.restoration.INotificationRestoreProcessor
import com.onesignal.onesignal.notification.internal.restoration.INotificationRestoreWorkManager
import com.onesignal.onesignal.notification.internal.restoration.impl.NotificationRestoreProcessor
import com.onesignal.onesignal.notification.internal.summary.INotificationSummaryManager

object NotificationModule {
    fun register(builder: ServiceBuilder) {
        builder.register<NotificationRestoreWorkManager>().provides<INotificationRestoreWorkManager>()
        builder.register<NotificationQueryHelper>().provides<INotificationQueryHelper>()
        builder.register<BadgeCountUpdater>().provides<IBadgeCountUpdater>()
        builder.register<NotificationDataController>().provides<INotificationDataController>()
        builder.register<NotificationGenerationWorkManager>().provides<INotificationGenerationWorkManager>()
        builder.register<NotificationBundleProcessor>().provides<INotificationBundleProcessor>()
        builder.register<NotificationChannelManager>().provides<INotificationChannelManager>()
        builder.register<NotificationLimitManager>().provides<INotificationLimitManager>()

        builder.register<NotificationDisplayer>().provides<INotificationDisplayer>()
        builder.register<SummaryNotificationDisplayer>().provides<ISummaryNotificationDisplayer>()
        builder.register<NotificationDisplayBuilder>().provides<INotificationDisplayBuilder>()

        builder.register<NotificationGenerationProcessor>().provides<INotificationGenerationProcessor>()
        builder.register<NotificationRestoreProcessor>().provides<INotificationRestoreProcessor>()
        builder.register<NotificationSummaryManager>().provides<INotificationSummaryManager>()

        builder.register<NotificationOpenedProcessor>().provides<INotificationOpenedProcessor>()
        builder.register<NotificationOpenedProcessorHMS>().provides<INotificationOpenedProcessorHMS>()

        builder.register<NotificationPermissionController>()
               .provides<INotificationPermissionController>()
               .provides<IBootstrapService>()

        builder.register<NotificationLifecycleService>()
               .provides<INotificationLifecycleService>()

        builder.register {
            if(FirebaseAnalyticsTracker.canTrack())
                return@register FirebaseAnalyticsTracker(
                                    it.getService(IApplicationService::class.java),
                                    it.getService(IPreferencesService::class.java),
                                    it.getService(ITime::class.java))
            else
                return@register NoAnalyticsTracker()
        }
               .provides<IAnalyticsTracker>()

        builder.register<ReceiveReceiptWorkManager>().provides<IReceiveReceiptWorkManager>()
        builder.register<ReceiveReceiptProcessor>().provides<IReceiveReceiptProcessor>()

        builder.register<NotificationsManager>()
               .provides<INotificationsManager>()
               .provides<IStartableService>()
    }
}