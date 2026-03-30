.PHONY: backend frontend

backend:
	cd dex-tracker-backend && mvn spring-boot:run

frontend:
	cd dex-tracker-frontend && npm run dev
