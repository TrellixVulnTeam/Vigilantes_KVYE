import * as cdk from "@aws-cdk/core";
import * as s3 from "@aws-cdk/aws-s3";
import * as lambda from "@aws-cdk/aws-lambda";
import { PythonFunction } from "@aws-cdk/aws-lambda-python";

export class DataInfraStack extends cdk.Stack {
	constructor(scope: cdk.Construct, id: string, props?: cdk.StackProps) {
		super(scope, id, props);

		// create three 3 buckets for each stage
		const queriesBucket = new s3.Bucket(this, "vigilantes-queries", {
			bucketName: "vigilantes-queries",
			versioned: true,
		});

		const scrapedImagesBucket = new s3.Bucket(
			this,
			"vigilantes-scraped-images",
			{
				bucketName: "vigilantes-scraped-images",
				versioned: true,
			}
		);

		const processedImagesBucket = new s3.Bucket(
			this,
			"vigilantes-processed-images",
			{
				bucketName: "vigilantes-processed-images",
				versioned: true,
			}
		);

		// create lambda function for each stage
		const readInputLambda = new PythonFunction(
			this,
			"vigilantes-data-infra-read-input",
			{
				entry: "lambdas/read_input.py",
				handler: "lambda_handler",
				runtime: lambda.Runtime.PYTHON_3_9,
				environment: {
					SRC_BUCKET_NAME: queriesBucket.bucketName,
				},
			}
		);
		const scrapeImagesLambda = new PythonFunction(
			this,
			"vigilantes-data-infra-scrape-images",
			{
				entry: "lambdas/scrape_images.py",
				handler: "lambda_handler",
				runtime: lambda.Runtime.PYTHON_3_9,
				environment: {
					DEST_BUCKET_NAME: scrapedImagesBucket.bucketName,
				},
			}
		);

		// grant lambda functions permission to s3 buckets
		queriesBucket.grantRead(readInputLambda);
		processedImagesBucket.grantWrite(scrapeImagesLambda);
	}
}
