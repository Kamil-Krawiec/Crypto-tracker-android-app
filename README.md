Project title: CryptoTrack - personal Cryptocurrency portfolio & Price alert app



Short desc: help users monitor their cryptocurrency investments and receive real-time price alerts. The app aims to provide a user-friendly interface for tracking portfolio performance, setting customizable alerts, and accessing up-to-date market data.

Functionalities:

Portfolio Management: Allow users to add and manage their cryptocurrency holdings, view current values, and track performance over time.

Price Alerts: Enable users to set custom price thresholds for various cryptocurrencies and receive notifications when these thresholds are crossed.

Market Data Integration: Fetch real-time price data from public APIs to ensure users have the latest information.

User Interface: Develop a sleek, intuitive UI with multiple views, ensuring responsiveness across different device configurations and orientations.

Features

Dashboard View: Display an overview of the user’s portfolio, including total value, individual asset performance, and recent market trends.

Add/Edit Assets: Provide forms for users to input their cryptocurrency holdings, including coin type, quantity, and purchase price.

Price Alert Configuration: Allow users to set upper and lower price limits for selected cryptocurrencies and manage existing alerts.

Notifications: Implement push notifications to alert users when price thresholds are met.

Implementation:
Data Storage: Utilize SQLite for local storage of user portfolio data and alert configurations.

API Integration: Fetch real-time cryptocurrency data from a public API such as CoinGecko or CoinMarketCap.

Notifications: Use Android’s NotificationManager to handle and display price alerts.

UI Components: Employ RecyclerView for dynamic lists, ViewPager for swipeable views, and ConstraintLayout for responsive design.
